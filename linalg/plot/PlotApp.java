package linalg.plot;


import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;


public class PlotApp extends Application {
    static String DEFAULT_TITLE = "Plot from java program";

    static Queue<Plot> plots = new ConcurrentLinkedQueue();


    public PlotApp(){     
        super();
    }

    @Override
    public void start(Stage stage) {

        new AnimationTimer() {
            final long ONE_SECOND =  1000000000;
            long start;

            public void handle(long now){
                if (now - start > ONE_SECOND){
                    start = now;
                    while(!plots.isEmpty())
                        draw(plots.poll());
                }
            }
        }.start();
    }

    public static void show(Plot plot){
        plots.offer(plot);
    }

    private void draw(Plot plot){

        Group root = new Group();
        root.getChildren().add(plot);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        if (plot.title() == null) stage.setTitle(DEFAULT_TITLE);
        else stage.setTitle(plot.title());

        stage.show();

        if (plot.name() != null){
           WritableImage wim = new WritableImage((int)plot.getWidth(),(int)plot.getHeight());
           plot.snapshot(null,wim); 

           File file = new File(plot.name()+".png");

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
            } catch (Exception s) {
                System.out.println(s);
            }
        }
    }
}

