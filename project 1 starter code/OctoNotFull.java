import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoNotFull extends Octo {

    public OctoNotFull(String id, Point position,
                List<PImage> images, int resourceLimit, int resourceCount,
                int actionPeriod, int animationPeriod){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    public void executeOctoNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
                Fish.class);

        if (!notFullTarget.isPresent() ||
                !this.moveToNotFull(scheduler, world, notFullTarget.get()) ||
                !this.transformNotFull(scheduler, world, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, imageStore, world),
                    this.getActionPeriod());
        }
    }

    public boolean transformNotFull(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceCount())
        {
            Entity octo = OctoFull.createOctoFull(this.getId(), this.getResourceLimit(),
                    this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                    this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(octo);
            this.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public boolean moveToNotFull(EventScheduler scheduler, WorldModel world, Entity target)
    {
        if (this.getPosition().adjacent(target.getPosition()))
        {
            this.setResourceCount(this.getResourceCount()+1);
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

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


    public static Octo createOctoNotFull(String id, int resourceLimit,
                                         Point position, int actionPeriod, int animationPeriod,
                                         List<PImage> images)
    {
        return new Octo(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

}
