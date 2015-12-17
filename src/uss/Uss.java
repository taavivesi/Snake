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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Uss extends Application {

	// Defineerin suunad.
	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	// Määran suuruse kastidena.
	public static final int BLOCK_SIZE = 15;
	public static final int APP_W = 40 * BLOCK_SIZE;
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

	String yellow = "-fx-background-color: lightyellow;";
	String black = "-fx-background-color: black;";
	String green = "-fx-background-color: green;";
	String color;

	// Loon vaadeldava listi.
	private ObservableList<Node> snake;

	// Teen valmis ussi, toidu ja kujunduse.
	private Parent content() {

		// Kujundus.
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
		points.setTranslateX(APP_W / 1.15);
		points.setTranslateY(2);
		controls.setTextFill(Color.WHITE);

		Line rLine = new Line(APP_W, 0, APP_W, APP_H);
		rLine.setStroke(Color.CHARTREUSE);
		rLine.setStrokeWidth(5);
		Line lLine = new Line(1, 1, 1, APP_H);
		lLine.setStroke(Color.CHARTREUSE);
		lLine.setStrokeWidth(5);
		Line tLine = new Line(1, 1, APP_W, 1);
		tLine.setStroke(Color.CHARTREUSE);
		tLine.setStrokeWidth(5);
		Line bLine = new Line(0, APP_H, APP_W, APP_H);
		bLine.setStroke(Color.CHARTREUSE);
		bLine.setStrokeWidth(5);

		// Tekib toit.
		Circle food = new Circle(BLOCK_SIZE / 2, BLOCK_SIZE / 2, 5);
		food.setFill(Color.CHARTREUSE);
		food.setTranslateX((int) (Math.random() * (APP_W - BLOCK_SIZE) / BLOCK_SIZE) * BLOCK_SIZE);
		food.setTranslateY((int) (Math.random() * (APP_H - BLOCK_SIZE) / BLOCK_SIZE) * BLOCK_SIZE);
		Rfood = 1;

		// Aja algus.
		KeyFrame frame = new KeyFrame(Duration.seconds(0.1), event -> {
			if (!running)
				return;

			// Kui ussi pikkus on 1 liigub ise edasi, kui 2 või enam võtab
			// viimase tüki ja tõstab ette.
			boolean toRemove = snake.size() > 1;

			Node tail = toRemove ? snake.remove(snake.size() - 1) : snake.get(0);

			// Jätan meelde ussi kordinaadid
			double tailX = tail.getTranslateX();
			double tailY = tail.getTranslateY();

			// kuhu ja kuidas uss liigub iga suuna korral
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

			// Lisame tagasi äravõetud tüki
			if (toRemove)
				snake.add(0, tail);

			// Kui uss vastu iseennast
			for (Node part : snake) {
				if (part != tail && tail.getTranslateX() == part.getTranslateX()
						&& tail.getTranslateY() == part.getTranslateY()) {
					stopGame();
					over.setTextFill(Color.WHITE);
					over.setText("GAME OVER !");
					over.setTranslateX(APP_W / 2.5);
					over.setTranslateY(APP_H / 4);
					over.setVisible(true);
					break;
				}

				// Kui uss läheb vastu seina
				if (tail.getTranslateX() < 0 || tail.getTranslateX() == APP_W || tail.getTranslateY() < 0
						|| tail.getTranslateY() == APP_H) {
					stopGame();
					over.setTextFill(Color.WHITE);
					over.setText("GAME OVER ! ");
					over.setTranslateX(APP_W / 2.25);
					over.setTranslateY(APP_H / 4);
					over.setVisible(true);
					break;
				}
			}

			// Kui uss läheb vastu boonus toitu
			if (tail.getTranslateX() == bonus.getTranslateX() && tail.getTranslateY() == bonus.getTranslateY()) {
				bonus.setVisible(false);
				bonus.isVisible();
				bonus.setDisable(true);
				bonus.isDisable();

				if (bonusFood == 1) {

					punktid = punktid + 50;
					points.setText("Punktid: " + punktid);
					points.setTextFill(Color.WHITE);
					points.setTranslateX(APP_W / 1.15);

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

				// Kui uss läheb vastu tavalist toitu
				if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
					food.setTranslateX((int) (Math.random() * (APP_W - BLOCK_SIZE) / BLOCK_SIZE) * BLOCK_SIZE);
					food.setTranslateY((int) (Math.random() * (APP_H - BLOCK_SIZE) / BLOCK_SIZE) * BLOCK_SIZE);

					punktid = punktid + 10;
					points.setText("Punktid: " + punktid);
					points.setTextFill(Color.WHITE);
					points.setTranslateX(APP_W / 1.15);
					points.setTranslateY(2);
					Circle circ = new Circle(BLOCK_SIZE / 2, BLOCK_SIZE / 2, 5, Color.WHITE);
					circ.setTranslateX(tailX);
					circ.setTranslateY(tailY);

					// Boonus toidu tekkimine
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

		// Keyframe käib lõpmatult
		timeline.getKeyFrames().add(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);

		window.getChildren().addAll(bonus, food, snakeBody, points, over, rLine, lLine, tLine, bLine);
		return bp;
	}

	// Juhised
	private static final String CONTROLLS = "CONTROLLS :\n" + "W - UP\n" + "S - DOWN\n" + "A - LEFT\n" + "D - RIGHT\n"
			+ "SPACE - RESTART GAME\n" + "ESC - EXIT GAME";

	// Mängu taaskäivitamine
	private void restartGame() {
		punktid = 0;
		points.setText("Punktid: " + punktid);
		points.setTextFill(Color.WHITE);
		points.setTranslateX(APP_W / 1.15);
		snake.clear();
		over.setVisible(false);
		over.isVisible();
		startGame();
	}

	// Mängu seisma jätmine
	private void stopGame() {
		running = false;
		timeline.stop();
	}

	// Mängu alustamine
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

		// Mängu aken
		Scene scene = new Scene(content());

		// Defineerin ära nupud
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

		// Menüü aken
		Pane p = new Pane();
		Image img = new Image("http://i.imgur.com/EroGfFb.jpg?1");
		ImageView imgView = new ImageView(img);

		Button button1 = new Button("START");
		button1.setOnAction(e -> {
			primaryStage.setScene(scene);
			startGame();
		});

		Button button2 = new Button("EXIT");
		button2.setOnAction(e -> System.exit(0));

		p.getChildren().addAll(imgView, button1, button2);
		Scene scene1 = new Scene(p, 500, 275);

		imgView.setLayoutX(100);
		button2.setLayoutX(325);
		button2.setLayoutY(200);
		button1.setLayoutX(150);
		button1.setLayoutY(200);
		primaryStage.setTitle("Uss");
		primaryStage.setScene(scene1);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
