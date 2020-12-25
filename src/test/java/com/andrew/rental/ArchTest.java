package com.andrew.rental;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.andrew.rental");

        noClasses()
            .that()
                .resideInAnyPackage("com.andrew.rental.service..")
            .or()
                .resideInAnyPackage("com.andrew.rental.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.andrew.rental.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
