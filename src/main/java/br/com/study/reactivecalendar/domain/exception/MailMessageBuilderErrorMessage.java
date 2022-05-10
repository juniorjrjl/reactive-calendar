package br.com.study.reactivecalendar.domain.exception;

public class MailMessageBuilderErrorMessage extends BaseErrorMessage{

    public static final MailMessageBuilderErrorMessage ABSTRACT_MAIL_WITHOUT_REQUIRED_PROPERTY = new MailMessageBuilderErrorMessage("abstractMailMessageBuilder.requiredField");
    public static final MailMessageBuilderErrorMessage NEW_INVITE_WITHOUT_REQUIRED_PROPERTY = new MailMessageBuilderErrorMessage("newAppointmentBuilder.requiredField");

    public static final MailMessageBuilderErrorMessage CANCEL_INVITE_WITHOUT_REQUIRED_PROPERTY = new MailMessageBuilderErrorMessage("cancelAppointmentBuilder.requiredField");

    public MailMessageBuilderErrorMessage(final String key) {
        super(key);
    }

    public MailMessageBuilderErrorMessage destinations(){
        this.params(getResource().getString("abstractMailMessageBuilder.destinations"));
        return this;
    }

    public MailMessageBuilderErrorMessage subject(){
        this.params(getResource().getString("abstractMailMessageBuilder.subject"));
        return this;
    }

    public MailMessageBuilderErrorMessage template(){
        this.params(getResource().getString("abstractMailMessageBuilder.template"));
        return this;
    }

    public MailMessageBuilderErrorMessage title(){
        this.params(getResource().getString("appointmentBuilder.title"));
        return this;
    }

    public MailMessageBuilderErrorMessage details(){
        this.params(getResource().getString("appointmentBuilder.details"));
        return this;
    }

    public MailMessageBuilderErrorMessage startIn(){
        this.params(getResource().getString("appointmentBuilder.startIn"));
        return this;
    }

    public MailMessageBuilderErrorMessage endIn(){
        this.params(getResource().getString("appointmentBuilder.endIn"));
        return this;
    }

    public MailMessageBuilderErrorMessage owner(){
        this.params(getResource().getString("appointmentBuilder.owner"));
        return this;
    }

}
