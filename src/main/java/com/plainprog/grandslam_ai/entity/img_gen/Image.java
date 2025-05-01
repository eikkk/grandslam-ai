package com.plainprog.grandslam_ai.entity.img_gen;

import com.plainprog.grandslam_ai.entity.account.Account;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_account_id")
    private Account creatorAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private ImgGenModule imgGenModule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private ImgGenProvider imgGenProvider;

    @Column(name = "price")
    private Double price;

    @Column(name = "orientation")
    private String orientation; // "v" or "h" or "s"

    @Column(name = "seed")
    private Integer seed;

    @Column(name = "fullsize")
    private String fullsize;

    @Column(name = "compressed")
    private String compressed;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "prompt")
    private String prompt;

    @Column(name = "negative_prompt")
    private String negativePrompt;

    @Column(name = "steps")
    private Integer steps;

    public Image() {
    }

    public Image(Double price, String orientation, Integer seed, String fullsize, String compressed, String thumbnail, Instant createdAt, String prompt, String negativePrompt, Integer steps) {
        this.price = price;
        this.orientation = orientation;
        this.seed = seed;
        this.fullsize = fullsize;
        this.compressed = compressed;
        this.thumbnail = thumbnail;
        this.createdAt = createdAt;
        this.prompt = prompt;
        this.negativePrompt = negativePrompt;
        this.steps = steps;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Account getCreatorAccount() {
        return creatorAccount;
    }

    public void setCreatorAccount(Account creatorAccount) {
        this.creatorAccount = creatorAccount;
    }

    public ImgGenModule getImgGenModule() {
        return imgGenModule;
    }

    public void setImgGenModule(ImgGenModule imgGenModule) {
        this.imgGenModule = imgGenModule;
    }

    public ImgGenProvider getImgGenProvider() {
        return imgGenProvider;
    }

    public void setImgGenProvider(ImgGenProvider imgGenProvider) {
        this.imgGenProvider = imgGenProvider;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public String getFullsize() {
        return fullsize;
    }

    public void setFullsize(String fullsize) {
        this.fullsize = fullsize;
    }

    public String getCompressed() {
        return compressed;
    }

    public void setCompressed(String compressed) {
        this.compressed = compressed;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getNegativePrompt() {
        return negativePrompt;
    }

    public void setNegativePrompt(String negativePrompt) {
        this.negativePrompt = negativePrompt;
    }
    public Integer getSteps() {
        return steps;
    }
    public void setSteps(Integer steps) {
        this.steps = steps;
    }
}
