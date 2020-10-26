import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


public abstract class Entity
{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

   public Entity(String id, Point position,
      List<PImage> images, int resourceLimit, int resourceCount,
      int actionPeriod, int animationPeriod)
   {
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
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
        return animationPeriod;
   }

   public void nextImage()
   {
      this.imageIndex = (this.imageIndex + 1) % this.images.size();
   }



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
       if (this instanceof OctoFull){
           scheduler.scheduleEvent(this,
                   Activity.createActivityAction(this, imageStore, world),
                   this.actionPeriod);
           scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0),
                   this.getAnimationPeriod());
       }
       if (this instanceof OctoNotFull) {
           scheduler.scheduleEvent(this,
                   Activity.createActivityAction(this, imageStore, world),
                   this.actionPeriod);
           scheduler.scheduleEvent(this,
                   Animation.createAnimationAction(this, 0), this.getAnimationPeriod());
       } if (this instanceof Fish){
       scheduler.scheduleEvent(this,
               Activity.createActivityAction(this, imageStore, world),
               this.actionPeriod);
       }
       if (this instanceof Crab){
           scheduler.scheduleEvent(this,
                   Activity.createActivityAction(this, imageStore, world),
                   this.actionPeriod);
           scheduler.scheduleEvent(this,
                   Animation.createAnimationAction(this, 0), this.getAnimationPeriod());
       }
       if (this instanceof Quake){
           scheduler.scheduleEvent(this,
                   Activity.createActivityAction(this, imageStore, world),
                   this.actionPeriod);
           scheduler.scheduleEvent(this,
                   Animation.createAnimationAction(this, Quake.QUAKE_ANIMATION_REPEAT_COUNT),
                   this.getAnimationPeriod());
       }
       if (this instanceof SGrass){
           scheduler.scheduleEvent(this,
                   Activity.createActivityAction(this, imageStore, world),
                   this.actionPeriod);
       }
       if (this instanceof Atlantis){
           scheduler.scheduleEvent(this,
                   Animation.createAnimationAction(this, Atlantis.ATLANTIS_ANIMATION_REPEAT_COUNT),
                   this.getAnimationPeriod());
       }

   }

}
