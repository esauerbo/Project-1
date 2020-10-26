import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoFull extends Octo {

    public OctoFull(String id, Point position,
                       List<PImage> images, int resourceLimit, int resourceCount,
                       int actionPeriod, int animationPeriod){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    public void executeOctoFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(),
                Atlantis.class);

        if (fullTarget.isPresent() &&
                moveToFull(scheduler, world, fullTarget.get()))
        {
            //at atlantis trigger animation
            fullTarget.get().scheduleActions(scheduler, world, imageStore);

            //transform to unfull
            this.transformFull(scheduler, world, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, imageStore, world),
                    this.getActionPeriod());
        }
    }


    public void transformFull(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        Entity octo = OctoNotFull.createOctoNotFull(this.getId(), this.getResourceLimit(),
                this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        this.scheduleActions(scheduler, world, imageStore);
    }

    public static Octo createOctoFull(String id, int resourceLimit,
                                      Point position, int actionPeriod, int animationPeriod,
                                      List<PImage> images)
    {
        return new Octo(id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }

    public boolean moveToFull(EventScheduler scheduler, WorldModel world, Entity target)
    {
        if (this.getPosition().adjacent(target.getPosition()))
        {
            return true;
        }
        else
        {
            Point nextPos = this.nextPositionOcto(world, target.getPosition());

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
