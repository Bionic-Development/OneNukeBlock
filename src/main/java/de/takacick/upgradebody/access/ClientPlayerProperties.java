package de.takacick.upgradebody.access;

import de.takacick.upgradebody.client.model.BodyEntityModel;

public interface ClientPlayerProperties {

    void setUpgradeModel(BodyEntityModel model);

    BodyEntityModel getUpgradeModel();

    void refreshEntityModel();

}
