package atp.project.part3.atpprojectpartc.View;

import atp.project.part3.atpprojectpartc.Model.IModel;
import atp.project.part3.atpprojectpartc.ViewModel.MyViewModel;


public interface IView {

    /**
     * Connects the ViewModel and Model to the View so it can use them.
     */
    void setViewModel(MyViewModel vm, IModel model);

    /**
     * Sets up the initial state of the View when it first loads.
     */
    void initialize();


}
