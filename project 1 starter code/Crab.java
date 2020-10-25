import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Crab extends Moved {

    public static final String CRAB_KEY = "crab";
    public static final String CRAB_ID_SUFFIX = " -- crab";
    public static final int CRAB_PERIOD_SCALE = 4;
    public static final int CRAB_ANIMATION_MIN = 50;
    public static final int CRAB_ANIMATION_MAX = 150;



    public Crab(String id, Point position, List<PImage> images, int actionPeriod,
                int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);

    }

    public static Crab createCrab(String id, Point position,
                                    int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Crab(id, position, images,
                 actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> crabTarget = world.findNearest(
                this.getPosition(), SGrass.class);
        long nextPeriod = this.getActionPeriod();

        if (crabTarget.isPresent())
        {
            Point tgtPos = crabTarget.get().getPosition();

            if (this.moveToCrab(world, crabTarget.get(), scheduler))
            {
                Entity quake = Quake.createQuake(tgtPos,
                        imageStore.getImageList(Quake.QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.getActionPeriod();
                this.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public boolean moveToCrab(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

}
