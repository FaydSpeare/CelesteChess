import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        int size = 400;

        HBox root = new HBox();
        BoardUI b = new BoardUI(size);
        root.getChildren().add(b);

        primaryStage.setScene(new Scene(root, size, size));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
