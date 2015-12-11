package uss;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Uss extends Application {

	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	public static final int BLOCK_SIZE = 15;
	public static final int APP_W = 50 * BLOCK_SIZE;
	public static final int APP_H = 30 * BLOCK_SIZE;

	private Direction direction = Direction.RIGHT;
	private boolean moved = false;
	private boolean running = false;
	int punktid;
	Label label = new Label();
	Label label2 = new Label();
	private Timeline timeline = new Timeline();

	private ObservableList<Node> snake;

	private Parent createContent() {
		Pane window = new Pane();
		window.setPrefSize(APP_W, APP_H);
		window.setStyle("-fx-background-color: black;");
		Group snakeBody = new Group();
		snake = snakeBody.getChildren();
		label.setText("Punktid " + punktid);
		label.setTextFill(Color.WHITE);
		label.setTranslateX(APP_W / 1.1);

		Circle food= new Circle(BLOCK_SIZE, BLOCK_SIZE, 5);
		food.setFill(Color.rgb(100,200,100));
		food.setTranslateX((int) (Math.random() * (APP_W - BLOCK_SIZE)) / BLOCK_SIZE * BLOCK_SIZE);
		food.setTranslateY((int) (Math.random() * (APP_H - BLOCK_SIZE)) / BLOCK_SIZE * BLOCK_SIZE);

		KeyFrame frame = new KeyFrame(Duration.seconds(0.1), event -> {
			if (!running)
				return;

			boolean toRemove = snake.size() > 1;

			Node tail = toRemove ? snake.remove(snake.size() - 1) : snake.get(0);

			double tailX = tail.getTranslateX();
			double tailY = tail.getTranslateY();

			switch (direction) {
			case UP:
				tail.setTranslateX(snake.get(0).getTranslateX());
				tail.setTranslateY(snake.get(0).getTranslateY() - BLOCK_SIZE);
				break;
			case DOWN:
				tail.setTranslateX(snake.get(0).getTranslateX());
				tail.setTranslateY(snake.get(0).getTranslateY() + BLOCK_SIZE);
				break;
			case LEFT:
				tail.setTranslateX(snake.get(0).getTranslateX() - BLOCK_SIZE);
				tail.setTranslateY(snake.get(0).getTranslateY());
				break;
			case RIGHT:
				tail.setTranslateX(snake.get(0).getTranslateX() + BLOCK_SIZE);
				tail.setTranslateY(snake.get(0).getTranslateY());
				break;
			}

			moved = true;

			if (toRemove)
				snake.add(0, tail);
			for (Node rect : snake) {
				if (rect != tail && tail.getTranslateX() == rect.getTranslateX()
						&& tail.getTranslateY() == rect.getTranslateY()) {
					stopGame();
					label2.setTextFill(Color.WHITE);
					label2.setText("GAME OVER !");
					label2.setTranslateX(APP_W /2.5);
					label2.setTranslateY(APP_H/4);
					label2.setVisible(true);
					break;
				}

				if (tail.getTranslateX() < 0 || tail.getTranslateX() >= APP_W || tail.getTranslateY() < 0
						|| tail.getTranslateY() >= APP_H) {
					stopGame();
					label2.setTextFill(Color.WHITE);
					label2.setText("GAME OVER ! ");
					label2.setTranslateX(APP_W /2.25);
					label2.setTranslateY(APP_H/4);
					label2.setVisible(true);
					break;
				}
			}

			if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
				food.setTranslateX((int) (Math.random() * (APP_W - BLOCK_SIZE)) / BLOCK_SIZE * BLOCK_SIZE);
				food.setTranslateY((int) (Math.random() * (APP_H - BLOCK_SIZE)) / BLOCK_SIZE * BLOCK_SIZE);
	
				punktid = punktid + 10;
				label.setText("Punktid " + punktid);
				label.setTextFill(Color.WHITE);
				label.setTranslateX(APP_W / 1.2);
				Circle circ = new Circle(BLOCK_SIZE, BLOCK_SIZE, 5, Color.WHITE);
				circ.setTranslateX(tailX);
				circ.setTranslateY(tailY);
				
				snake.add(circ);

			}
		});

		timeline.getKeyFrames().add(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);

		window.getChildren().addAll(food, snakeBody, label, label2);
		return window;
	}

	private void restartGame() {
		punktid = 0;
		snake.clear();
		label2.setVisible(false);
		label2.isVisible();
		startGame();
	}

	
	private void stopGame() {
		running = false;
		timeline.stop();
	}

	private void startGame() {
		direction = Direction.RIGHT;
		Circle head = new Circle(BLOCK_SIZE, BLOCK_SIZE, 5, Color.WHITE);
		snake.add(head);
		timeline.play();
		running = true;
	}
	


	@SuppressWarnings("incomplete-switch")
	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(createContent());
		scene.setOnKeyPressed(event -> {
			if(!moved)
				return;
			
				switch(event.getCode()){
				case W:
					if(direction != Direction.DOWN)
						direction = Direction.UP;
					break;
				case S:
					if (direction != Direction.UP)
						direction = Direction.DOWN;
					break;
				case A:
					if (direction != Direction.RIGHT)
						direction = Direction.LEFT;
					break;
				case D:
					if (direction != Direction.LEFT)
						direction = Direction.RIGHT;
					break;
				case SPACE:
					restartGame();
					break;
				}
			
		});
		
		
		Pane sp = new Pane();
		Image img =new Image("http://i.imgur.com/EroGfFb.jpg?1");
		ImageView imgView = new ImageView(img);
		
		Button button1 = new Button("START");
		button1.setOnAction(e -> primaryStage.setScene(scene));
		
		Button button2 = new Button ("EXIT");
		button2.setOnAction(e -> System.exit(0));
		
		sp.getChildren().addAll(imgView,button1,button2);
		Scene scene1 = new Scene(sp,500,200);
		
		imgView.setLayoutX(100);
		button2.setLayoutX(325);
		button2.setLayoutY(150);
		button1.setLayoutX(125);
		button1.setLayoutY(150);
		primaryStage.setTitle("Uss");
		primaryStage.setScene(scene1);
		primaryStage.show();
		startGame();

	}

	public static void main(String[] args) {
		launch(args);
		}
}
