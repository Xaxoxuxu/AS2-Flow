package com.as2flow.views;

import com.as2flow.backend.entity.PartnershipEntity;
import com.helger.as2lib.crypto.ECryptoAlgorithmCrypt;
import com.helger.as2lib.crypto.ECryptoAlgorithmSign;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class PartnershipForm extends FormLayout
{
    TextField name = new TextField("Name");
    TextField senderAs2Id = new TextField("Sender AS2 ID");
    TextField receiverAs2Id = new TextField("Receiver AS2 ID");
    TextField as2Url = new TextField("AS2 URL");
    TextField subject = new TextField("Subject");
    ComboBox<ECryptoAlgorithmCrypt> cryptoAlgorithm = new ComboBox<>("Encryption Algorithm");
    ComboBox<ECryptoAlgorithmSign> signingAlgorithm = new ComboBox<>("Signing Algorithm");
    TextField senderX509Alias = new TextField("Sender certificate alias");
    TextField receiverX509Alias = new TextField("Receiver certificate alias");
    EmailField senderEmail = new EmailField("Sender Email");
    EmailField receiverEmail = new EmailField("Receiver Email");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Close");
    Binder<PartnershipEntity> binder = new BeanValidationBinder<>(PartnershipEntity.class);
    private PartnershipEntity partnershipEntity;

    public PartnershipForm()
    {
        addClassName("form");

        name.setPlaceholder("Name");
        senderAs2Id.setPlaceholder("Sender");
        receiverAs2Id.setPlaceholder("Receiver");
        as2Url.setPlaceholder("URL");
        subject.setPlaceholder("Subject");
        senderX509Alias.setPlaceholder("Cert Alias");
        receiverX509Alias.setPlaceholder("Cert Alias");
        senderEmail.setPlaceholder("Email");
        receiverEmail.setPlaceholder("Email");

        binder.bind(name, PartnershipEntity::getName, null);
        binder.bind(senderAs2Id, PartnershipEntity::getSenderAs2Id, PartnershipEntity::setSenderAs2Id);
        binder.bind(receiverAs2Id, PartnershipEntity::getReceiverAs2Id, PartnershipEntity::setReceiverAs2Id);
        binder.bind(as2Url, PartnershipEntity::getAs2Url, PartnershipEntity::setAs2Url);
        binder.bind(subject, PartnershipEntity::getSubject, PartnershipEntity::setSubject);
        binder.bind(senderX509Alias, PartnershipEntity::getSenderX509Alias, PartnershipEntity::setSenderX509Alias);
        binder.bind(receiverX509Alias, PartnershipEntity::getReceiverX509Alias, PartnershipEntity::setReceiverX509Alias);
        binder.bind(senderEmail, PartnershipEntity::getSenderEmail, PartnershipEntity::setSenderEmail);
        binder.bind(receiverEmail, PartnershipEntity::getReceiverEmail, PartnershipEntity::setReceiverEmail);
        binder.bind(cryptoAlgorithm, PartnershipEntity::getEncryptAlgorithm, PartnershipEntity::setEncryptAlgorithm);
        binder.bind(signingAlgorithm, PartnershipEntity::getSigningAlgorithm, PartnershipEntity::setSigningAlgorithm);

        cryptoAlgorithm.setItems(ECryptoAlgorithmCrypt.values());
        signingAlgorithm.setItems(ECryptoAlgorithmSign.values());

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(new HorizontalLayout(name, senderAs2Id, receiverAs2Id, as2Url, subject));
        verticalLayout.add(new HorizontalLayout(cryptoAlgorithm, signingAlgorithm, senderX509Alias, receiverX509Alias));
        verticalLayout.add(new HorizontalLayout(senderEmail, receiverEmail));
        verticalLayout.add(createButtonsLayout());

        add(verticalLayout);
    }

    private HorizontalLayout createButtonsLayout()
    {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, partnershipEntity)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave()
    {
        try
        {
            binder.writeBean(partnershipEntity);
            fireEvent(new SaveEvent(this, partnershipEntity));
        } catch (ValidationException e)
        {
            e.printStackTrace();
        }
    }

    public void setPartnership(PartnershipEntity partnershipEntity)
    {
        this.partnershipEntity = partnershipEntity;
        binder.readBean(partnershipEntity);
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener)
    {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class PartnershipFormEvent extends ComponentEvent<PartnershipForm>
    {
        private final PartnershipEntity partnershipEntity;

        protected PartnershipFormEvent(PartnershipForm source, PartnershipEntity partnershipEntity)
        {
            super(source, false);
            this.partnershipEntity = partnershipEntity;
        }

        public PartnershipEntity getPartnership()
        {
            return partnershipEntity;
        }
    }

    public static class SaveEvent extends PartnershipFormEvent
    {
        SaveEvent(PartnershipForm source, PartnershipEntity partnershipEntity)
        {
            super(source, partnershipEntity);
        }
    }

    public static class DeleteEvent extends PartnershipFormEvent
    {
        DeleteEvent(PartnershipForm source, PartnershipEntity partnershipEntity)
        {
            super(source, partnershipEntity);
        }

    }

    public static class CloseEvent extends PartnershipFormEvent
    {
        CloseEvent(PartnershipForm source)
        {
            super(source, null);
        }
    }
}