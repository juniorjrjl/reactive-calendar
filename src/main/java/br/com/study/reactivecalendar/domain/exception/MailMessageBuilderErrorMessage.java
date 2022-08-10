package br.com.study.reactivecalendar.domain.exception;

public class MailMessageBuilderErrorMessage extends BaseErrorMessage{

    public static final MailMessageBuilderErrorMessage MAIL_WITHOUT_REQUIRED_PROPERTY = new MailMessageBuilderErrorMessage("mailMessageDTOBuilder.requiredField");

    public MailMessageBuilderErrorMessage(final String key) {
        super(key);
    }

    public MailMessageBuilderErrorMessage destinations(){
        this.params(getResource().getString("mailMessageDTOBuilder.destinations"));
        return this;
    }

    public MailMessageBuilderErrorMessage subject(){
        this.params(getResource().getString("mailMessageDTOBuilder.subject"));
        return this;
    }

    public MailMessageBuilderErrorMessage template(){
        this.params(getResource().getString("mailMessageDTOBuilder.template"));
        return this;
    }

    public MailMessageBuilderErrorMessage title(){
        this.params(getResource().getString("mailMessageDTOBuilder.title"));
        return this;
    }

    public MailMessageBuilderErrorMessage details(){
        this.params(getResource().getString("mailMessageDTOBuilder.details"));
        return this;
    }

    public MailMessageBuilderErrorMessage startIn(){
        this.params(getResource().getString("mailMessageDTOBuilder.startIn"));
        return this;
    }

    public MailMessageBuilderErrorMessage endIn(){
        this.params(getResource().getString("mailMessageDTOBuilder.endIn"));
        return this;
    }

    public MailMessageBuilderErrorMessage owner(){
        this.params(getResource().getString("mailMessageDTOBuilder.owner"));
        return this;
    }

}
