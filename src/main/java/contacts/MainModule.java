package contacts;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import contacts.client.*;
import contacts.client.activity.Activity;
import contacts.client.activity.ListActivity;
import contacts.client.activity.MainActivity;
import contacts.client.activity.RecordActivity;
import contacts.controller.ContactsController;
import contacts.repository.ContactsRepository;
import contacts.repository.InMemoryContactsRepository;
import contacts.service.ClassFieldSetterProvider;
import contacts.service.ContactsService;
import contacts.service.PhoneNumberValidator;
import contacts.client.CommandLineUserInterface;

import javax.inject.Singleton;

public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().requireExplicitBindings();
        install(new FactoryModuleBuilder()
                .implement(Activity.class, Names.named("main"), MainActivity.class)
                .implement(Activity.class, Names.named("list"), ListActivity.class)
                .implement(Activity.class, Names.named("record"), RecordActivity.class)
                .build(ActivityFactory.class));
        bind(CommandLineUserInterface.class).in(Singleton.class);
        bind(ContactsRepository.class).to(InMemoryContactsRepository.class).in(Singleton.class);
        bind(ClassFieldSetterProvider.class).in(Singleton.class);
        bind(PhoneNumberValidator.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    ContactsService contactsService(ContactsRepository repository,
                                    PhoneNumberValidator phoneNumberValidator,
                                    ClassFieldSetterProvider classFieldSetterProvider) {
        return new ContactsService(repository, phoneNumberValidator, classFieldSetterProvider);
    }

    @Provides
    @Singleton
    ContactsController contactsController(ContactsService contactsService) {
        return new ContactsController(contactsService);
    }
}
