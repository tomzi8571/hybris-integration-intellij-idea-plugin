package com.intellij.idea.plugin.hybris.project.tasks;

import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.project.settings.DefaultHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.HybrisImportParameters;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.utils.HybrisProjectUtils;
import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.Messages;
import com.intellij.projectImport.ProjectImportBuilder;
import com.intellij.util.Processor;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created 6:07 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class SearchModulesRootsTaskModalWindow extends Task.Modal {

    private static final Logger LOG = Logger.getInstance(SearchModulesRootsTaskModalWindow.class);

    protected final File rootProjectDirectory;
    protected final HybrisImportParameters projectImportParameters;

    public SearchModulesRootsTaskModalWindow(
        @NotNull final File rootProjectDirectory,
        @NotNull final HybrisImportParameters projectImportParameters
    ) {
        super(
            ProjectImportBuilder.getCurrentProject(),
            HybrisI18NBundleUtils.message("hybris.project.import.scanning"),
            true
        );

        Validate.notNull(rootProjectDirectory);
        Validate.notNull(projectImportParameters);

        this.rootProjectDirectory = rootProjectDirectory;
        this.projectImportParameters = projectImportParameters;
    }

    @Override
    public void run(@NotNull final ProgressIndicator indicator) {
        Validate.notNull(indicator);

        this.projectImportParameters.getFoundModules().clear();
        this.projectImportParameters.setRootDirectory(null);

        final List<File> moduleRootDirectories = HybrisProjectUtils.findModuleRoots(
            this.rootProjectDirectory, new ProgressIndicatorUpdaterProcessor(indicator)
        );

        final List<HybrisModuleDescriptor> moduleDescriptors = new ArrayList<HybrisModuleDescriptor>();
        final Collection<File> pathsFailedToImport = new ArrayList<File>();

        for (File moduleRootDirectory : moduleRootDirectories) {
            try {
                moduleDescriptors.add(new DefaultHybrisModuleDescriptor(moduleRootDirectory));
            } catch (HybrisConfigurationException e) {
                LOG.error("Can not import a module using path: " + pathsFailedToImport, e);

                pathsFailedToImport.add(moduleRootDirectory);
            }
        }

        if (!pathsFailedToImport.isEmpty()) {
            this.showErrorMessage(pathsFailedToImport);
        }

        Collections.sort(moduleDescriptors);

        HybrisProjectUtils.buildDependencies(moduleDescriptors);

        this.projectImportParameters.getFoundModules().addAll(moduleDescriptors);
        this.projectImportParameters.setRootDirectory(this.rootProjectDirectory);
    }

    protected void showErrorMessage(@NotNull final Collection<File> directoriesFailedToImport) {
        Validate.notNull(directoriesFailedToImport);

        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                Messages.showErrorDialog(
                    HybrisI18NBundleUtils.message("hybris.project.import.failed", directoriesFailedToImport),
                    HybrisI18NBundleUtils.message("hybris.project.error")
                );
            }
        });
    }

    @Override
    public void onCancel() {
        this.projectImportParameters.getFoundModules().clear();
        this.projectImportParameters.setRootDirectory(null);
    }

    protected class ProgressIndicatorUpdaterProcessor implements Processor<File> {

        protected final ProgressIndicator progressIndicator;

        public ProgressIndicatorUpdaterProcessor(@NotNull final ProgressIndicator indicator) {
            Validate.notNull(indicator);

            this.progressIndicator = indicator;
        }

        @Override
        public boolean process(final File t) {
            if (this.progressIndicator.isCanceled()) {
                return false;
            }

            this.progressIndicator.setText2(t.getAbsolutePath());

            return true;
        }
    }
}