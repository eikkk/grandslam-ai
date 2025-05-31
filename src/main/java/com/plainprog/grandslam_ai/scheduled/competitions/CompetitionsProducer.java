package com.plainprog.grandslam_ai.scheduled.competitions;


import com.plainprog.grandslam_ai.entity.competitions.Competition;
import com.plainprog.grandslam_ai.entity.competitions.CompetitionQueue;
import com.plainprog.grandslam_ai.entity.competitions.CompetitionTheme;
import com.plainprog.grandslam_ai.entity.competitions.CompetitionThemeGroup;
import com.plainprog.grandslam_ai.service.competition.CompetitionDrawBuilderService;
import com.plainprog.grandslam_ai.service.competition.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
@Component
public class CompetitionsProducer {
    private static final int DEFAULT_COMPETITION_SLOTS = 8;
    private static final int DEFAULT_COMPETITION_VOTE_TARGET = 2;

    @Autowired
    private CompetitionService competitionService;
    @Autowired
    CompetitionDrawBuilderService competitionDrawBuilderService;

    @Scheduled(fixedRate = 30000) // Executes every 30s for now
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public synchronized void competitionsProducer() {
        System.out.println("Running competitionsProducer: " + System.currentTimeMillis());
        // Get all competition with status STARTED
        List<Competition> startedCompetitions = competitionService.getCompetitionsByStatus(Competition.CompetitionStatus.STARTED);
        for(Competition competition : startedCompetitions) {
            System.out.println("Competition with status STARTED found: " + competition.getId() + " - " + competition.getTheme().getName());
            try {
                competitionDrawBuilderService.buildCompetitionDraw(competition).get(); // Await execution
            } catch (Exception e) {
                System.err.println("Error while building competition draw: " + e.getMessage());
            }
        }
        // Check if a new competition has to be opened
        List<CompetitionThemeGroup> groupsWithOpenCompetitions = competitionService.getAllThemeGroupsByCompetitionStatus(Competition.CompetitionStatus.OPEN);
        System.out.println("Groups with open competitions: " + groupsWithOpenCompetitions.size() + " - " + groupsWithOpenCompetitions.stream().map(g -> g.getId()).toList());

        // Get all active theme groups
        List<CompetitionThemeGroup> allActiveThemeGroups = competitionService.getAllActiveThemeGroups();
        System.out.println("Total active theme groups: " + allActiveThemeGroups.size() + " - " + allActiveThemeGroups.stream().map(g -> g.getId()).toList());

        // Check groups without open competitions
        System.out.println("Checking for groups without open competitions...");
        for (CompetitionThemeGroup group : allActiveThemeGroups) {
            if (!groupsWithOpenCompetitions.contains(group)) {
                System.out.println("Found group without open competition: " + group.getId() + " - " + group.getName());
                handleGroupWithoutOpenCompetition(group);
            }
        }
        System.out.println("competitionsProducer completed at: " + System.currentTimeMillis());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    private void handleGroupWithoutOpenCompetition(CompetitionThemeGroup group) {
        List<Competition> openCompetitions = competitionService.getCompetitionsByStatusAndThemeGroupId(Competition.CompetitionStatus.OPEN, group.getId());

        if (!openCompetitions.isEmpty()) {
            System.out.println("Found existing OPEN competition for group: " + group.getId() + ", skipping");
            return;
        }
        System.out.println("Handling group without open competition: " + group.getId() + " - " + group.getName());

        // Get all themes of this group
        List<CompetitionTheme> availableThemes = competitionService.getAllActiveThemesByGroup(group);
        System.out.println("Available themes for group " + group.getId() + ": " + availableThemes.size() + " - " + availableThemes.stream().map(t -> t.getId()).toList());

        // Check queue
        CompetitionQueue queueItemToProcess = null;
        List<CompetitionQueue> awaitingQueue = competitionService.getAllNotProcessedCompetitionsFromQueue(group.getId());
        System.out.println("Awaiting queue items for group " + group.getId() + ": " + awaitingQueue.size());

        if (awaitingQueue.isEmpty()) {
            System.out.println("No awaiting queue items found, creating new queue item");
            queueItemToProcess = produceAndInsertNewQueueItem(availableThemes, group);
        } else {
            System.out.println("Using existing queue item: " + awaitingQueue.get(0).getId());
            queueItemToProcess = awaitingQueue.get(0);
        }

        // Create competition
        System.out.println("Opening competition from queue item: " + queueItemToProcess.getId());
        openCompetitionFromQueueItem(queueItemToProcess);

        // Check if we need to add more to queue
        CompetitionQueue finalQueueItemToProcess = queueItemToProcess;
        awaitingQueue = awaitingQueue.stream()
                .filter(item -> !item.equals(finalQueueItemToProcess))
                .toList();

        System.out.println("Remaining queue items after processing: " + awaitingQueue.size());
        if (awaitingQueue.isEmpty()){
            System.out.println("No more queue items, adding new one to queue");
            produceAndInsertNewQueueItem(availableThemes, group);
        }

        System.out.println("Finished handling group: " + group.getId());
    }

    @Transactional
    private CompetitionQueue produceAndInsertNewQueueItem(List<CompetitionTheme> availableThemes, CompetitionThemeGroup group) {
        System.out.println("Producing new queue item for group: " + group.getId());

        var queueHistory = competitionService.getLastProcessedQueueItemsOfGivenGroup(availableThemes.size(), group.getId());
        System.out.println("Queue history size: " + queueHistory.size());

        List<CompetitionTheme> themeHistory = queueHistory.stream()
                .map(CompetitionQueue::getTheme)
                .toList();
        System.out.println("Theme history IDs: " + themeHistory.stream().map(t -> t.getId()).toList());

        var theme = produceNextCompetitionTheme(themeHistory, availableThemes);
        System.out.println("Selected theme for new queue item: " + theme.getId() + " - " + theme.getName());

        CompetitionQueue newQueueItem = new CompetitionQueue(theme, CompetitionQueue.CompetitionQueueStatus.NEW);
        CompetitionQueue savedItem = competitionService.saveCompetitionQueue(newQueueItem);
        System.out.println("Saved new queue item with ID: " + savedItem.getId());

        return savedItem;
    }

    @Transactional
    private void openCompetitionFromQueueItem(CompetitionQueue queueItem) {
        System.out.println("Opening competition from queue item: " + queueItem.getId() + " with theme: " + queueItem.getTheme().getId());

        Competition competition = new Competition(queueItem.getTheme(), DEFAULT_COMPETITION_SLOTS, DEFAULT_COMPETITION_VOTE_TARGET);
        competition = competitionService.saveCompetition(competition);
        System.out.println("Created new competition with ID: " + competition.getId() + ", slots: " + DEFAULT_COMPETITION_SLOTS);

        queueItem.setCompetition(competition);
        queueItem.setStatus(CompetitionQueue.CompetitionQueueStatus.PROCESSED);
        queueItem.setProcessedAt(Instant.now());
        competitionService.saveCompetitionQueue(queueItem);
        System.out.println("Updated queue item status to PROCESSED, processed at: " + queueItem.getProcessedAt());
    }

    private CompetitionTheme produceNextCompetitionTheme(List<CompetitionTheme> themeHistory, List<CompetitionTheme> availableThemes) {
        System.out.println("Producing next competition theme from " + availableThemes.size() + " available themes");

        if (availableThemes.size() == 1) {
            System.out.println("Only one theme available, returning it: " + availableThemes.get(0).getId());
            return availableThemes.get(0);
        }

        // Check if there is any theme that is not in the history
        for (CompetitionTheme theme : availableThemes) {
            if (!themeHistory.contains(theme)) {
                System.out.println("Found theme not in history: " + theme.getId() + " - returning it");
                return theme;
            }
        }

        System.out.println("All themes are in history, selecting based on probability");

        // Filter out not available themes from history
        themeHistory = themeHistory.stream()
                .filter(theme -> availableThemes.stream().anyMatch(t -> t.getId().equals(theme.getId())))
                .toList();
        System.out.println("Filtered theme history size: " + themeHistory.size());

        // Create a frequency map
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (CompetitionTheme theme : themeHistory) {
            Integer id = theme.getId();
            frequencyMap.put(id, frequencyMap.getOrDefault(id, 0) + 1);
        }
        System.out.println("Theme frequency map: " + frequencyMap);

        // Build probability map
        Map<Integer, Double> probabilityMap = new HashMap<>();
        List<Integer> historyIds = themeHistory.stream().map(CompetitionTheme::getId).toList();

        int baseWeight = (historyIds.size() + 1) * (historyIds.size() + 1);
        System.out.println("Base weight for calculation: " + baseWeight);

        for (CompetitionTheme theme : availableThemes) {
            probabilityMap.put(theme.getId(), (double) baseWeight);
        }

        Set<Integer> processedIds = new HashSet<>();
        for (int i = 0; i < historyIds.size(); i++) {
            Integer id = historyIds.get(i);
            if (processedIds.contains(id)) {
                continue;
            }

            double position = i + 1;
            int frequency = frequencyMap.get(id);
            double weight = Math.pow(position, 2) / Math.pow(frequency, 2);

            System.out.println("Theme ID " + id + ": position=" + position + ", frequency=" + frequency + ", weight=" + weight);
            probabilityMap.put(id, weight);
            processedIds.add(id);
        }

        double sum = probabilityMap.values().stream().mapToDouble(Double::doubleValue).sum();
        System.out.println("Sum of weights: " + sum);

        for (Integer id : probabilityMap.keySet()) {
            probabilityMap.put(id, probabilityMap.get(id) / sum);
        }
        System.out.println("Probability map: " + probabilityMap);

        double randomValue = Math.random();
        System.out.println("Random value: " + randomValue);

        double cumulativeProbability = 0.0;
        for (CompetitionTheme theme : availableThemes) {
            cumulativeProbability += probabilityMap.get(theme.getId());
            System.out.println("Theme " + theme.getId() + " cumulative probability: " + cumulativeProbability);
            if (randomValue <= cumulativeProbability) {
                System.out.println("Selected theme: " + theme.getId());
                return theme;
            }
        }

        System.out.println("Fallback: returning first available theme: " + availableThemes.get(0).getId());
        return availableThemes.get(0);
    }
}