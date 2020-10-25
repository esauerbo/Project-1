import processing.core.PImage;
import java.util.Optional;
import java.util.List;

public class OctoFull extends Octo {

    public OctoFull(String id, Point position, List<PImage> images, int actionPeriod,
                       int animationPeriod, int resourceLimit, int resourceCount) {
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit, resourceCount);
    }


    public static OctoFull createOctoFull(String id, int resourceLimit,
                                        Point position, int actionPeriod, int animationPeriod,
                                        List<PImage> images)
    {
        return new OctoFull(id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }

    public void executeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(), Atlantis.class);

        if (fullTarget.isPresent() &&
                moveToFull( world, fullTarget.get(), scheduler))
        {
            this.transformFull(scheduler, world, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    public void transformFull(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        OctoNotFull octo = OctoNotFull.createOctoNotFull(this.getId(), this.resourceLimit,
                this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        this.scheduleActions(scheduler, world, imageStore);
    }

    public  boolean moveToFull(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition()))
        {
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition( world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant( nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents( occupant.get());
                }

                world.moveEntity( this, nextPos);
            }
            return false;
        }
    }
}
