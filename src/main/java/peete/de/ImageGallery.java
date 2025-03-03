package peete.de;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileFilter;

public class ImageGallery extends Application {

    private int currentImageIndex = 0;
    private File[] imageFiles;
    private ImageView fullImageView;
    private StackPane root;
    private Button prevButton, nextButton;
    private BorderPane fullView; // Declare fullView as a class variable

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Image Gallery");

        // Load images from the resources folder
        File folder = new File("src/main/resources/images");
        imageFiles = folder.listFiles((FileFilter) pathname -> {
            String name = pathname.getName().toLowerCase();
            return name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg");
        });

        if (imageFiles == null || imageFiles.length == 0) {
            showError("No images found in the directory.");
            return;
        }

        // Thumbnail Grid
        GridPane grid = createThumbnailGrid();

        // Full-Size Image View
        fullView = createFullImageView(); // Initialize fullView here

        // Main Layout (StackPane to switch between grid and full view)
        root = new StackPane();
        root.getStyleClass().add("root");

        // Add heading and grid to a VBox
        Label heading = new Label("FOOD MENU");
        heading.getStyleClass().add("heading");

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("scroll-pane");

        VBox thumbnailView = new VBox(10, heading, scrollPane);
        thumbnailView.setAlignment(Pos.CENTER);
        thumbnailView.setPadding(new Insets(20));

        root.getChildren().add(thumbnailView);

        // Scene
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/Gridd.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createThumbnailGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        int col = 0, row = 0;
        for (File file : imageFiles) {
            Image thumbnailImg = new Image(file.toURI().toString(), 150, 0, true, true);
            ImageView thumbnail = new ImageView(thumbnailImg);
            thumbnail.getStyleClass().add("thumbnail");

            thumbnail.setOnMouseClicked(e -> showFullImage(file));
            grid.add(thumbnail, col++, row);
            if (col == 4) {
                col = 0;
                row++;
            }
        }

        return grid;
    }

    private BorderPane createFullImageView() {
        BorderPane fullView = new BorderPane();
        fullView.getStyleClass().add("full-view");

        // Full-Size Image
        fullImageView = new ImageView();
        fullImageView.setPreserveRatio(true);
        fullImageView.setFitWidth(700);
        fullImageView.setFitHeight(500);

        //Button
        prevButton = new Button("Previous");
        Button backButton = new Button("Back to Thumbnails");
        nextButton = new Button("Next");

        prevButton.setOnAction(e -> showPreviousImage());
        backButton.setOnAction(e -> showThumbnailGrid());
        nextButton.setOnAction(e -> showNextImage());

        HBox controls = new HBox(10, prevButton, backButton, nextButton);
        controls.setAlignment(Pos.CENTER);
        controls.getStyleClass().add("controls");

        fullView.setCenter(fullImageView);
        fullView.setBottom(controls);

        return fullView;
    }
    private void showFullImage(File file) {
        Image image = new Image(file.toURI().toString());
        fullImageView.setImage(image);
        //image
        for (int i = 0; i < imageFiles.length; i++) {
            if (imageFiles[i].equals(file)) {
                currentImageIndex = i;
                break;
            }
        }

        updateButtonStates();

        // Switch to full view
        root.getChildren().setAll(fullView);
    }

    private void showThumbnailGrid() {
        Label heading = new Label("FOODMENU");
        heading.getStyleClass().add("heading");

        GridPane grid = createThumbnailGrid();
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("scroll-pane");

        VBox thumbnailView = new VBox(10, heading, scrollPane);
        thumbnailView.setAlignment(Pos.CENTER);
        thumbnailView.setPadding(new Insets(20));

        root.getChildren().setAll(thumbnailView); // Replace the content of the StackPane
    }

    private void showPreviousImage() {
        if (currentImageIndex > 0) {
            currentImageIndex--;
            showFullImage(imageFiles[currentImageIndex]);
        }
    }

    private void showNextImage() {
        if (currentImageIndex < imageFiles.length - 1) {
            currentImageIndex++;
            showFullImage(imageFiles[currentImageIndex]);
        }
    }

    private void updateButtonStates() {
        prevButton.setDisable(currentImageIndex == 0);
        nextButton.setDisable(currentImageIndex == imageFiles.length - 1);
    }

    private void showError(String message) {
        Label errorLabel = new Label(message);
        errorLabel.getStyleClass().add("error");
        root.getChildren().setAll(errorLabel);
    }
}