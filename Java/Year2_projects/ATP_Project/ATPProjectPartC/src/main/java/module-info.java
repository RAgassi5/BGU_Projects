module atp.project.part3.atpprojectpartc {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires ATPProjectJAR;
    requires java.desktop;
    requires javafx.media;
    requires java.logging;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;


    exports atp.project.part3.atpprojectpartc;

    opens atp.project.part3.atpprojectpartc.View to javafx.fxml;
}