/*
Action: ideally what our various entities might do in our virutal world
 */

public abstract class Action
{
   //protected ActionKind kind;
   protected Entity entity;
   protected WorldModel world;
   protected ImageStore imageStore;
   protected int repeatCount;

   public Action(Entity entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }

   public Entity getEntity() {
      return entity;
   }
   public WorldModel getWorld() {
      return world;
   }
   public ImageStore getImageStore() {
      return imageStore;
   }
   public int getRepeatCount() {
      return repeatCount;
   }

   public abstract void executeAction(EventScheduler scheduler);


}
