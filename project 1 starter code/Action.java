/*
Action: ideally what our various entities might do in our virutal world
 */

public abstract class Action
{

   protected Entity entity;
   protected WorldModel world;
   protected ImageStore imageStore;
   protected int repeatCount;

   public Action(Entity entity, WorldModel world, ImageStore imageStore, int repeatCount)
   {
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }

   public Entity getEntity(){return entity;}

   abstract public void executeAction(EventScheduler scheduler);

}
