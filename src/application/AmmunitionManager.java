package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;

public class AmmunitionManager {
    private HBox ammunitionBox;
    private int bulletCount;
    private AudioClip shootSound;
    private AudioClip reloadSound;
    private AudioClip empty;

    public AmmunitionManager(HBox ammunitionBox, int initialBulletCount) {
        this.ammunitionBox = ammunitionBox;
        this.bulletCount = initialBulletCount;

        for (int i = 0; i < initialBulletCount; i++) {
            Image ammunitionImage = new Image("Ammunition.png");
            ImageView ammunitionImageView = new ImageView(ammunitionImage);
            ammunitionImageView.setFitWidth(50);
            ammunitionImageView.setFitHeight(50);
            ammunitionBox.getChildren().add(ammunitionImageView);
        }
        shootSound = new AudioClip(getClass().getResource("/Shot.mp3").toString());
        reloadSound = new AudioClip(getClass().getResource("/Reload.mp3").toString());
        empty = new AudioClip(getClass().getResource("/empty magazine.mp3").toString());
    }

    public boolean removeBullet() {
        if (bulletCount > 0) {
            ammunitionBox.getChildren().remove(0);
            bulletCount--;
            shootSound.play();
            return true;
        } else {
            System.out.println("No bullets left!");
            empty.play();
            return false;
        }
    }

    public void addBullet() {
        if (bulletCount < 8) {
            Image ammunitionImage = new Image("Ammunition.png");
            ImageView ammunitionImageView = new ImageView(ammunitionImage);
            ammunitionImageView.setFitWidth(50);
            ammunitionImageView.setFitHeight(50);
            ammunitionBox.getChildren().add(ammunitionImageView);
            bulletCount++;
            
        }
    }

    public void reloadBullets() {
        int bulletsToAdd = 8 - bulletCount;

        for (int i = 0; i < bulletsToAdd; i++) {
            Image ammunitionImage = new Image("Ammunition.png");
            ImageView ammunitionImageView = new ImageView(ammunitionImage);
            ammunitionImageView.setFitWidth(50);
            ammunitionImageView.setFitHeight(50);
            ammunitionBox.getChildren().add(ammunitionImageView);
        }
        reloadSound.play();
        bulletCount = 8;
    }

    public int getBulletCount() {
        return bulletCount;
    }
    public void clearBullets() {
        ammunitionBox.getChildren().clear();
        bulletCount = 0;
    }
    public void resetAmmunition() {
        ammunitionBox.getChildren().clear();
        for (int i = 0; i < 8; i++) {
            Image ammunitionImage = new Image("Ammunition.png");
            ImageView ammunitionImageView = new ImageView(ammunitionImage);
            ammunitionImageView.setFitWidth(50);
            ammunitionImageView.setFitHeight(50);
            ammunitionBox.getChildren().add(ammunitionImageView);
        }
        bulletCount = 8;
    }
}
