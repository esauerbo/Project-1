import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


public abstract class Entity
{

    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;
    private String id;

    public Entity( String id, Point position,
                   List<PImage> images, int actionPeriod, int animationPeriod)
    {
        this.id=id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    /*-----------Getters-----------*/
    public int getActionPeriod(){ return actionPeriod; }
    public List<PImage> getImages() { return this.images; }
    int getImageIndex(){ return imageIndex; }
    public Point getPosition() { return this.position;}
    public int getAnimationPeriod() { return animationPeriod; }
    public String getId(){ return id; }


    public void setPosition(Point p) { this.position = p; }

    public void nextImage()
    {
        this.imageIndex = (this.getImageIndex()+ 1) % this.getImages().size();
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
}
