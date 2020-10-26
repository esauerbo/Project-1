import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


public abstract class Entity
{
    private EntityKind kind;
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

   public Entity(EntityKind kind, String id, Point position,
      List<PImage> images, int resourceLimit, int resourceCount,
      int actionPeriod, int animationPeriod)
   {
      this.kind = kind;
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }

    public EntityKind getKind() {
        return kind;
    }
    public String getId() {
        return id;
    }
    public Point getPosition() {
        return position;
    }
    public List<PImage> getImages() {
        return images;
    }
    public int getImageIndex() {
        return imageIndex;
    }
    public int getResourceLimit() {
        return resourceLimit;
    }
    public int getResourceCount() {
        return resourceCount;
    }
    public int getActionPeriod() {
        return actionPeriod;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getAnimationPeriod()
   {
      switch (this.kind)
      {
         case OCTO_FULL:
         case OCTO_NOT_FULL:
         case CRAB:
         case QUAKE:
         case ATLANTIS:
            return this.animationPeriod;
         default:
            throw new UnsupportedOperationException(
                    String.format("getAnimationPeriod not supported for %s",
                            this.kind));
      }
   }

   public void nextImage()
   {
      this.imageIndex = (this.imageIndex + 1) % this.images.size();
   }







/*   public Action createAnimationAction(int repeatCount)
   {
      return new Animation(ActionKind.ANIMATION, this,null, null, repeatCount);
   }*/


   public static PImage getCurrentImage(Object entity)
   {
      if (entity instanceof Background)
      {
         return ((Background)entity).images
                 .get(((Background)entity).imageIndex);
      }
      else if (entity instanceof Entity)
      {
         return ((Entity)entity).images.get(((Entity)entity).imageIndex);
      }
      else
      {
         throw new UnsupportedOperationException(
                 String.format("getCurrentImage not supported for %s",
                         entity));
      }
   }

   public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
   {
      switch (this.kind)
      {
         case OCTO_FULL:
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, imageStore, world),
                    this.actionPeriod);
            scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0),
                    this.getAnimationPeriod());
            break;

         case OCTO_NOT_FULL:
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, imageStore, world),
                    this.actionPeriod);
            scheduler.scheduleEvent(this,
                    Animation.createAnimationAction(this, 0), this.getAnimationPeriod());
            break;

         case FISH:
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, imageStore, world),
                    this.actionPeriod);
            break;

         case CRAB:
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, imageStore, world),
                    this.actionPeriod);
            scheduler.scheduleEvent(this,
                    Animation.createAnimationAction(this, 0), this.getAnimationPeriod());
            break;

         case QUAKE:
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, imageStore, world),
                    this.actionPeriod);
            scheduler.scheduleEvent(this,
                    Animation.createAnimationAction(this, Quake.QUAKE_ANIMATION_REPEAT_COUNT),
                    this.getAnimationPeriod());
            break;

         case SGRASS:
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, imageStore, world),
                    this.actionPeriod);
            break;
         case ATLANTIS:
            scheduler.scheduleEvent(this,
                    Animation.createAnimationAction(this, Atlantis.ATLANTIS_ANIMATION_REPEAT_COUNT),
                    this.getAnimationPeriod());
            break;

         default:
      }
   }




}
