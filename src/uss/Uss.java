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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
	private Timeline timeline = new Timeline();
	private boolean moved = false;
	private boolean running = false;
	int punktid;
	int bonusFood;
	int Rfood;
	Label points = new Label();
	Label over = new Label();
	Label controls = new Label(CONTROLLS);
	Rectangle bonus = new Rectangle(9, 9);
	double dif = 0.1;
	ComboBox<String> comboBox;
	ComboBox<String> comboBox2;
	static double easy = 0.2;
	static double medium = 0.1;
	static double hard = 0.05;
	String yellow = "-fx-background-color: lightyellow;";
	String black = "-fx-background-color: black;";
	String green = "-fx-background-color: green;";
	String color;

	private ObservableList<Node> snake;

	private Parent createContent() {

		BorderPane bp = new BorderPane();
		Pane side = new Pane(controls);
		side.setStyle("-fx-background-color: darkblue;");
		Pane window = new Pane();
		window.setPrefSize(APP_W, APP_H);
		window.setStyle(black);
		bp.setCenter(window);
		bp.setLeft(side);
		Group snakeBody = new Group();
		snake = snakeBody.getChildren();
		points.setText("Punktid: " + punktid);
		points.setTextFill(Color.WHITE);
		points.setTranslateX(APP_W / 1.1);
		controls.setTextFill(Color.WHITE);

		Circle food = new Circle(BLOCK_SIZE / 2, BLOCK_SIZE / 2, 5);
		food.setFill(Color.rgb(100, 200, 100));
		food.setTranslateX((int) (Math.random() * (APP_W - BLOCK_SIZE) / BLOCK_SIZE) * BLOCK_SIZE);
		food.setTranslateY((int) (Math.random() * (APP_H - BLOCK_SIZE) / BLOCK_SIZE) * BLOCK_SIZE);
		Rfood = 1;

		KeyFrame frame = new KeyFrame(Duration.seconds(easy), event -> {
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
			for (Node border : snake) {
				if (border != tail && tail.getTranslateX() == border.getTranslateX()
						&& tail.getTranslateY() == border.getTranslateY()) {
					stopGame();
					over.setTextFill(Color.WHITE);
					over.setText("GAME OVER !");
					over.setTranslateX(APP_W / 2.5);
					over.setTranslateY(APP_H / 4);
					over.setVisible(true);

					break;
				}

				if (tail.getTranslateX() < 0 || tail.getTranslateX() >= APP_W || tail.getTranslateY() < 0
						|| tail.getTranslateY() >= APP_H) {
					stopGame();
					over.setTextFill(Color.WHITE);
					over.setText("GAME OVER ! ");
					over.setTranslateX(APP_W / 2.25);
					over.setTranslateY(APP_H / 4);
					over.setVisible(true);
					break;
				}
			}
			if (tail.getTranslateX() == bonus.getTranslateX() && tail.getTranslateY() == bonus.getTranslateY()) {
				bonus.setVisible(false);
				bonus.isVisible();
				bonus.setDisable(true);
				bonus.isDisable();

				if (bonusFood == 1) {

					punktid = punktid + 50;
					points.setText("Punktid: " + punktid);
					points.setTextFill(Color.WHITE);
					points.setTranslateX(APP_W / 1.1);
					Circle circ2 = new Circle(BLOCK_SIZE / 2, BLOCK_SIZE / 2, 5, Color.WHITE);
					circ2.setTranslateX(tailX);
					circ2.setTranslateY(tailY);
					Circle circ3 = new Circle(BLOCK_SIZE / 2, BLOCK_SIZE / 2, 5, Color.WHITE);
					circ3.setTranslateX(tailX);
					circ3.setTranslateY(tailY);
					snake.addAll(circ2, circ3);
					bonusFood = 0;
					Rfood = 1;
					food.setVisible(true);
				}
			}
			if (Rfood == 1) {

				if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
					food.setTranslateX((int) (Math.random() * (APP_W - BLOCK_SIZE) / BLOCK_SIZE) * BLOCK_SIZE);
					food.setTranslateY((int) (Math.random() * (APP_H - BLOCK_SIZE) / BLOCK_SIZE) * BLOCK_SIZE);

					punktid = punktid + 10;
					points.setText("Punktid: " + punktid);
					points.setTextFill(Color.WHITE);
					points.setTranslateX(APP_W / 1.1);
					Circle circ = new Circle(BLOCK_SIZE / 2, BLOCK_SIZE / 2, 5, Color.WHITE);
					circ.setTranslateX(tailX);
					circ.setTranslateY(tailY);

					if (7 < (int) (Math.random() * 10)) {
						Rfood = 0;
						bonusFood = 1;
						bonus.setVisible(true);
						bonus.setDisable(false);
						bonus.setFill(Color.RED);
						bonus.setTranslateX((int) (Math.random() * (APP_W - BLOCK_SIZE) / BLOCK_SIZE) * BLOCK_SIZE);
						bonus.setTranslateY((int) (Math.random() * (APP_H - BLOCK_SIZE) / BLOCK_SIZE) * BLOCK_SIZE);
						food.setVisible(false);
						food.isVisible();
					}
					snake.add(circ);
				}
			}
		});

		timeline.getKeyFrames().add(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);

		window.getChildren().addAll(bonus, food, snakeBody, points, over);
		return bp;
	}

	private static final String CONTROLLS = "CONTROLLS :\n" + "W - UP\n" + "S - DOWN\n" + "A - LEFT\n" + "D - RIGHT\n"
			+ "SPACE - RESTART GAME\n" + "ESC - EXIT GAME";

	private void restartGame() {
		punktid = 0;
		points.setText("Punktid: " + punktid);
		points.setTextFill(Color.WHITE);
		points.setTranslateX(APP_W / 1.1);
		snake.clear();
		over.setVisible(false);
		over.isVisible();
		startGame();
	}

	private void stopGame() {
		running = false;
		timeline.stop();
	}

	private void startGame() {
		direction = Direction.RIGHT;
		Circle head = new Circle(BLOCK_SIZE / 2, BLOCK_SIZE / 2, 5, Color.WHITE);
		snake.add(head);
		timeline.play();
		running = true;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(createContent());
		scene.setOnKeyPressed(event -> {
			if (!moved)
				return;

			switch (event.getCode()) {
			case W:
				if (direction != Direction.DOWN)
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
			case ESCAPE:
				System.exit(0);
			}

		});

		Pane p = new Pane();
		Image img = new Image("http://i.imgur.com/EroGfFb.jpg?1");
		ImageView imgView = new ImageView(img);

		Button button1 = new Button("START");
		button1.setOnAction(e -> primaryStage.setScene(scene));

		Button button2 = new Button("EXIT");
		button2.setOnAction(e -> System.exit(0));

		comboBox = new ComboBox<>();
		comboBox.getItems().addAll("Easy", "Medium", "Hard");

		comboBox.setPromptText("Select difficulty");
		comboBox.setOnAction(e -> {
			if (comboBox.getValue() == "Easy") {
				dif = easy;
				System.out.println("Test");
				System.out.println("dif: " + dif);
			}
			if (comboBox.getValue() == "Medium") {
				dif = medium;
				System.out.println("dif: " + dif);
			}
			if (comboBox.getValue() == "Hard") {
				dif = hard;
				System.out.println("dif: " + dif);
			}
		});

		comboBox2 = new ComboBox<>();
		comboBox2.getItems().addAll("Green", "Black", "Yellow");

		comboBox2.setPromptText("Select color");
		comboBox2.setOnAction(e -> {
			if (comboBox.getValue() == "Green") {
				color = green;
				System.out.println("Test");
				System.out.println("dif: " + dif);
			}
			if (comboBox.getValue() == "Black") {
				color = black;
				System.out.println("dif: " + dif);
			}
			if (comboBox.getValue() == "Yellow") {
				color = yellow;
				System.out.println("dif: " + dif);
			}
		});

		p.getChildren().addAll(imgView, button1, button2, comboBox, comboBox2);
		Scene scene1 = new Scene(p, 500, 275);

		comboBox2.setLayoutX(100);
		comboBox2.setLayoutY(150);
		comboBox.setLayoutX(290);
		comboBox.setLayoutY(150);
		imgView.setLayoutX(100);
		button2.setLayoutX(325);
		button2.setLayoutY(200);
		button1.setLayoutX(125);
		button1.setLayoutY(200);
		primaryStage.setTitle("Uss");
		primaryStage.setScene(scene1);
		primaryStage.show();
		startGame();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
