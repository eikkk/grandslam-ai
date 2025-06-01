package com.plainprog.grandslam_ai.helper.competition;

/**
 * Helper class for calculating ELO ratings after matches
 */
public class EloCalculator {
    // Default K-factor (determines how much a single match affects the rating)
    private static final int DEFAULT_K_FACTOR = 32;

    /**
     * Calculates new ELO ratings for two competitors after a match
     *
     * @param player1Elo Current ELO rating of player 1
     * @param player2Elo Current ELO rating of player 2
     * @param player1Won true if player 1 won, false if player 2 won
     * @return An array of two integers: [player1NewElo, player2NewElo]
     */
    public static int[] calculateNewRatings(int player1Elo, int player2Elo, boolean player1Won) {
        return calculateNewRatings(player1Elo, player2Elo, player1Won, DEFAULT_K_FACTOR);
    }

    /**
     * Calculates new ELO ratings with custom K-factor
     *
     * @param player1Elo Current ELO rating of player 1
     * @param player2Elo Current ELO rating of player 2
     * @param player1Won true if player 1 won, false if player 2 won
     * @param kFactor K-factor to use (higher values cause bigger rating changes)
     * @return An array of two integers: [player1NewElo, player2NewElo]
     */
    private static int[] calculateNewRatings(int player1Elo, int player2Elo, boolean player1Won, int kFactor) {
        // Calculate expected outcomes
        double expectedOutcome1 = calculateExpectedOutcome(player1Elo, player2Elo);
        double expectedOutcome2 = calculateExpectedOutcome(player2Elo, player1Elo);

        // Actual outcome: 1 for win, 0 for loss
        double actualOutcome1 = player1Won ? 1.0 : 0.0;
        double actualOutcome2 = player1Won ? 0.0 : 1.0;

        // Calculate new ratings
        int newPlayer1Elo = calculateNewRating(player1Elo, kFactor, actualOutcome1, expectedOutcome1);
        int newPlayer2Elo = calculateNewRating(player2Elo, kFactor, actualOutcome2, expectedOutcome2);

        return new int[]{newPlayer1Elo, newPlayer2Elo};
    }

    /**
     * Calculates the expected outcome of a match based on ELO ratings
     *
     * @param playerElo The ELO rating of the player
     * @param opponentElo The ELO rating of the opponent
     * @return The expected outcome (between 0 and 1)
     */
    private static double calculateExpectedOutcome(int playerElo, int opponentElo) {
        return 1.0 / (1.0 + Math.pow(10, (opponentElo - playerElo) / 400.0));
    }

    /**
     * Calculates the new rating for a player
     *
     * @param oldRating The current rating
     * @param k K-factor
     * @param actualOutcome The actual outcome (1 for win, 0 for loss)
     * @param expectedOutcome The expected outcome
     * @return The new rating
     */
    private static int calculateNewRating(int oldRating, int k, double actualOutcome, double expectedOutcome) {
        return (int) Math.round(oldRating + k * (actualOutcome - expectedOutcome));
    }
}