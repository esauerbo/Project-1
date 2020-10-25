import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoNotFull extends Octo {

    public OctoNotFull(String id, Point position, List<PImage> images, int actionPeriod,
                       int animationPeriod, int resourceLimit, int resourceCount) {
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit, resourceCount);
    }


    public static OctoNotFull createOctoNotFull(String id, int resourceLimit,
                                           Point position, int actionPeriod, int animationPeriod,
                                           List<PImage> images)
    {
        return new OctoNotFull(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
                Fish.class);

        if (!notFullTarget.isPresent() ||
                !this.moveToNotFull(scheduler, world, notFullTarget.get()) ||
                !this.transformNotFull(world,scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction((Entity)this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    public boolean moveToNotFull(EventScheduler scheduler, WorldModel world, Entity target)
    {
        if (this.getPosition().adjacent(target.getPosition()))
        {
            this.resourceCount += 1;
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



    public boolean transformNotFull(WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore) {
        if (this.getResourceCount() >= this.getResourceLimit()) {

            Entity octo = OctoFull.createOctoFull(this.getId(), this.getResourceLimit(),
                    this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                    this.getImages());

            world.removeEntity((Entity) this);
            scheduler.unscheduleAllEvents((Entity) this);

            world.addEntity(octo);

            ((OctoFull)octo).scheduleActions(scheduler, world, imageStore);

            return true;
        }
        return false;
    }
}
