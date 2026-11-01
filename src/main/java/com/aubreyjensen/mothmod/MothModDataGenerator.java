package com.aubreyjensen.mothmod;

// My imports
import com.aubreyjensen.mothmod.datagen.ModModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
/*
Generates the .json files for the new items so that I don't have to write them
 */
public class MothModDataGenerator implements DataGeneratorEntrypoint {
	// Runs whenever you select to run Data Generation
    @Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        // Looks at the ModModelProvider and the items I have added to that class,
        // and creates the .json files for them
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(ModModelProvider::new);
	}
}
