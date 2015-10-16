package com.github.quiram.buildhotspots.visualisation;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

/*
 * Class to represent a dependency between two build configurations
 */
@SuppressWarnings("restriction")
public class DependencyGroup extends Group {
    private BuildConfigurationGroup target = null;
    private BuildConfigurationGroup origin = null;

    Line m_l = null;
    ArrowHead m_srcArrow = null;
    ArrowHead m_targArrow = null;


    public DependencyGroup(BuildConfigurationGroup origin, BuildConfigurationGroup target) {
        this.target = target;
        this.origin = origin;

        m_l = new Line();
        //m_l.setStyle(m_data.getStyle());

        getChildren().add(m_l);

        m_targArrow = new ArrowHead();
        getChildren().add(m_targArrow);
    }

    public BuildConfigurationGroup getOrigin() {
        return origin;
    }

    public BuildConfigurationGroup getTarget() {
        return target;
    }

    class ArrowHead extends Group {
        Line m_l1 = new Line();
        Line m_l2 = new Line();
        Rotate m_r = new Rotate(0, 0, 0);

        public ArrowHead() {
            m_l1.setStartX(0);
            m_l1.setStartY(0);
            m_l1.setEndX(-5);
            m_l1.setEndY(10);

            m_l2.setStartX(0);
            m_l2.setStartY(0);
            m_l2.setEndX(5);
            m_l2.setEndY(10);

            //m_l1.setStyle(m_data.getStyle());
            //m_l2.setStyle(m_data.getStyle());

            this.getChildren().add(m_l1);
            this.getChildren().add(m_l2);

            this.getTransforms().add(m_r);
        }

        public void setAngle(double p_ang) {
            m_r.setAngle(p_ang);
        }
    }

    private double caculateAngle(Point2D p_src, Point2D p_targ) {
        double w = p_targ.getX() - p_src.getX();
        double h = p_targ.getY() - p_src.getY();

        double ang = 0;
        if (w < 0) {
            ang = Math.atan(h / w);
            ang -= (Math.PI / 2);
        } else if (w != 0) {
            ang = Math.atan(h / w);
            ang += (Math.PI / 2);
        }
        return ang;
    }

    /*
     * Used to move a point to the edge of the circle
     */
    private Point2D adjustPoint(Point2D p_origin, double p_direction, double p_amount) {
        return new Point2D(
                p_origin.getX() + (p_amount * Math.sin(p_direction)),
                p_origin.getY() - (p_amount * Math.cos(p_direction))
        );
    }

    public void Draw() {

        Point2D sourceCentre = new Point2D(target.getLayoutX(), target.getLayoutY());
        Point2D targetCentre = new Point2D(origin.getLayoutX(), origin.getLayoutY());

        double ang = caculateAngle(sourceCentre, targetCentre);
        Point2D actualStart = adjustPoint(sourceCentre, ang, target.getRadius());
        Point2D actualEnd = adjustPoint(targetCentre, ang + Math.PI, origin.getRadius());

        //Just an arrow from the source to the target
        m_l.setStartX(actualStart.getX());
        m_l.setStartY(actualStart.getY());
        m_l.setEndX(actualEnd.getX());
        m_l.setEndY(actualEnd.getY());

        if (m_srcArrow != null) {
            m_srcArrow.setTranslateX(actualStart.getX());
            m_srcArrow.setTranslateY(actualStart.getY());
            m_srcArrow.setAngle((ang * (180 / Math.PI)) + 180);
        }
        m_targArrow.setTranslateX(actualEnd.getX());
        m_targArrow.setTranslateY(actualEnd.getY());
        m_targArrow.setAngle(ang * (180 / Math.PI));
    }
}
