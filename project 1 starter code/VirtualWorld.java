import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import processing.core.*;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
   extends PApplet
{
   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;

   private static final int TIMER_ACTION_PERIOD = 100;

   private static final int VIEW_WIDTH = 640;
   private static final int VIEW_HEIGHT = 480;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 2;
   private static final int WORLD_HEIGHT_SCALE = 2;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private static final String IMAGE_LIST_FILE_NAME = "imagelist";
   private static final String DEFAULT_IMAGE_NAME = "background_default";
   private static final int DEFAULT_IMAGE_COLOR = 0x808080;

   private static final String LOAD_FILE_NAME = "world.sav";

   private static final String FAST_FLAG = "-fast";
   private static final String FASTER_FLAG = "-faster";
   private static final String FASTEST_FLAG = "-fastest";
   private static final double FAST_SCALE = 0.5;
   private static final double FASTER_SCALE = 0.25;
   private static final double FASTEST_SCALE = 0.10;

   private static double timeScale = 1.0;

   public ImageStore imageStore;
   public WorldModel world;
   private WorldView view;
   private EventScheduler scheduler;

   private long next_time;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         this.scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      view.drawViewport();
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }
         view.shiftView(dx, dy);
      }
   }

   private static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
              imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   private static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private static void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         world.load(in, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.entities)
      {
         //Only start actions for entities that include action (not those with just animations)
         if (entity.getActionPeriod() > 0)
            entity.scheduleActions(scheduler, world, imageStore);
      }
   }

   private static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static boolean parseBackground(String [] properties,
                                         WorldModel world, ImageStore imageStore)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         world.setBackground(pt,
                 new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   public static boolean parseOcto(String [] properties, WorldModel world,
                                   ImageStore imageStore)
   {
      if (properties.length == Octo.OCTO_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Octo.OCTO_COL]),
                 Integer.parseInt(properties[Octo.OCTO_ROW]));
         Entity entity = OctoNotFull.createOctoNotFull(properties[Octo.OCTO_ID],
                 Integer.parseInt(properties[Octo.OCTO_LIMIT]),
                 pt,
                 Integer.parseInt(properties[Octo.OCTO_ACTION_PERIOD]),
                 Integer.parseInt(properties[Octo.OCTO_ANIMATION_PERIOD]),
                 imageStore.getImageList(Octo.OCTO_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == Octo.OCTO_NUM_PROPERTIES;
   }

   public static boolean parseObstacle(String [] properties, WorldModel world,
                                       ImageStore imageStore)
   {
      if (properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[Obstacle.OBSTACLE_COL]),
                 Integer.parseInt(properties[Obstacle.OBSTACLE_ROW]));
         Entity entity = Obstacle.createObstacle(properties[Obstacle.OBSTACLE_ID],
                 pt, imageStore.getImageList(Obstacle.OBSTACLE_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES;
   }

   public static boolean parseFish(String [] properties, WorldModel world,
                                   ImageStore imageStore)
   {
      if (properties.length == Fish.FISH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Fish.FISH_COL]),
                 Integer.parseInt(properties[Fish.FISH_ROW]));
         Entity entity = Fish.createFish(properties[Fish.FISH_ID],
                 pt, Integer.parseInt(properties[Fish.FISH_ACTION_PERIOD]),
                 imageStore.getImageList(Fish.FISH_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == Fish.FISH_NUM_PROPERTIES;
   }

   public static boolean parseAtlantis(String [] properties, WorldModel world,
                                       ImageStore imageStore)
   {
      if (properties.length == Atlantis.ATLANTIS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Atlantis.ATLANTIS_COL]),
                 Integer.parseInt(properties[Atlantis.ATLANTIS_ROW]));
         Entity entity = Atlantis.createAtlantis(properties[Atlantis.ATLANTIS_ID],
                 pt, imageStore.getImageList(Atlantis.ATLANTIS_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == Atlantis.ATLANTIS_NUM_PROPERTIES;
   }

   public static boolean parseSgrass(String [] properties, WorldModel world,
                                     ImageStore imageStore)
   {
      if (properties.length == SGrass.SGRASS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SGrass.SGRASS_COL]),
                 Integer.parseInt(properties[SGrass.SGRASS_ROW]));
         Entity entity = SGrass.createSgrass(properties[SGrass.SGRASS_ID],
                 pt,
                 Integer.parseInt(properties[SGrass.SGRASS_ACTION_PERIOD]),
                 imageStore.getImageList(SGrass.SGRASS_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == SGrass.SGRASS_NUM_PROPERTIES;
   }



   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
