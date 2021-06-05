package com.as2flow.application.views;

import com.as2flow.application.backend.entity.Partnership;
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
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class PartnershipForm extends FormLayout
{
    private TextField name = new TextField("Name");
    private TextField senderAs2Id = new TextField("Sender AS2 ID");
    private TextField receiverAs2Id = new TextField("Receiver AS2 ID");
    private TextField as2Url = new TextField("AS2 URL");
    private TextField subject = new TextField("Subject");
    private ComboBox<ECryptoAlgorithmCrypt> cryptoAlgorithm = new ComboBox<>("Encryption Algorithm");
    private ComboBox<ECryptoAlgorithmSign> signingAlgorithm = new ComboBox<>("Signing Algorithm");
    private TextField senderX509Alias = new TextField("Sender certificate alias");
    private TextField receiverX509Alias = new TextField("Receiver certificate alias");
    private EmailField email = new EmailField("Email");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button close = new Button("Cancel");
    private Binder<Partnership> binder = new BeanValidationBinder<>(Partnership.class);
    private Partnership partnership;

    public PartnershipForm()
    {
        addClassName("partnership-form");

        binder.bind(name, Partnership::getName, Partnership::setName);
        binder.bind(senderAs2Id, Partnership::getSenderAs2Id, Partnership::setSenderAs2Id);
        binder.bind(receiverAs2Id, Partnership::getReceiverAs2Id, Partnership::setReceiverAs2Id);
        binder.bind(as2Url, Partnership::getAs2Url, Partnership::setAs2Url);
        binder.bind(subject, Partnership::getSubject, Partnership::setSubject);
        binder.bind(senderX509Alias, Partnership::getSenderX509Alias, Partnership::setSenderX509Alias);
        binder.bind(receiverX509Alias, Partnership::getReceiverX509Alias, Partnership::setReceiverX509Alias);
        binder.bind(email, Partnership::getEmail, Partnership::setEmail);

        cryptoAlgorithm.setItems(ECryptoAlgorithmCrypt.values());
        signingAlgorithm.setItems(ECryptoAlgorithmSign.values());

        add(senderAs2Id,
                receiverAs2Id,
                as2Url,
                subject,
                cryptoAlgorithm,
                signingAlgorithm,
                senderX509Alias,
                receiverX509Alias,
                email,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout()
    {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, partnership)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave()
    {
        try
        {
            binder.writeBean(partnership);
            fireEvent(new SaveEvent(this, partnership));
        } catch (ValidationException e)
        {
            e.printStackTrace();
        }
    }

    public void setPartnership(Partnership partnership)
    {
        this.partnership = partnership;
        binder.readBean(partnership);
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener)
    {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class PartnershipFormEvent extends ComponentEvent<PartnershipForm>
    {
        private final Partnership partnership;

        protected PartnershipFormEvent(PartnershipForm source, Partnership partnership)
        {
            super(source, false);
            this.partnership = partnership;
        }

        public Partnership getPartnership()
        {
            return partnership;
        }
    }

    public static class SaveEvent extends PartnershipFormEvent
    {
        SaveEvent(PartnershipForm source, Partnership partnership)
        {
            super(source, partnership);
        }
    }

    public static class DeleteEvent extends PartnershipFormEvent
    {
        DeleteEvent(PartnershipForm source, Partnership partnership)
        {
            super(source, partnership);
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