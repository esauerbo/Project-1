import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Octo extends Entity {

    public static final String OCTO_KEY = "octo";
    public static final int OCTO_NUM_PROPERTIES = 7;
    public static final int OCTO_ID = 1;
    public static final int OCTO_COL = 2;
    public static final int OCTO_ROW = 3;
    public static final int OCTO_LIMIT = 4;
    public static final int OCTO_ACTION_PERIOD = 5;
    public static final int OCTO_ANIMATION_PERIOD = 6;

    public Octo(EntityKind kind, String id, Point position,
                List<PImage> images, int resourceLimit, int resourceCount,
                int actionPeriod, int animationPeriod){
        super(kind, id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }


    public void executeOctoFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(),
                EntityKind.ATLANTIS);

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
                    world.createActivityAction(this, imageStore),
                    this.getActionPeriod());
        }
    }


    public void executeOctoNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
                EntityKind.FISH);

        if (!notFullTarget.isPresent() ||
                !this.moveToNotFull(scheduler, world, notFullTarget.get()) ||
                !this.transformNotFull(scheduler, world, imageStore))
        {
            scheduler.scheduleEvent(this,
                    world.createActivityAction(this, imageStore),
                    this.getActionPeriod());
        }
    }


    public Point nextPositionOcto(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.getPosition().x);
        Point newPos = new Point(this.getPosition().x + horiz,
                this.getPosition().y);

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.y - this.getPosition().y);
            newPos = new Point(this.getPosition().x,
                    this.getPosition().y + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = this.getPosition();
            }
        }

        return newPos;
    }


    public boolean transformNotFull(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceCount())
        {
            Entity octo = Octo.createOctoFull(this.getId(), this.getResourceLimit(),
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


    public void transformFull(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        Entity octo = Octo.createOctoNotFull(this.getId(), this.getResourceLimit(),
                this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        this.scheduleActions(scheduler, world, imageStore);
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

    public static Octo createOctoFull(String id, int resourceLimit,
                                        Point position, int actionPeriod, int animationPeriod,
                                        List<PImage> images)
    {
        return new Octo(EntityKind.OCTO_FULL, id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }

    public static Octo createOctoNotFull(String id, int resourceLimit,
                                           Point position, int actionPeriod, int animationPeriod,
                                           List<PImage> images)
    {
        return new Octo(EntityKind.OCTO_NOT_FULL, id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
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

