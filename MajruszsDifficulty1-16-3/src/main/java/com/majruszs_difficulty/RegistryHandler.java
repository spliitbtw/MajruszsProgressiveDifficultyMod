package com.majruszs_difficulty;

import com.majruszs_difficulty.commands.ChangeGameStateCommand;
import com.majruszs_difficulty.entities.EliteSkeletonEntity;
import com.majruszs_difficulty.entities.GiantEntity;
import com.majruszs_difficulty.entities.PillagerWolfEntity;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
	public static final DeferredRegister< EntityType< ? > > ENTITIES = DeferredRegister.create( ForgeRegistries.ENTITIES, MajruszsDifficulty.MOD_ID );
	public static final DeferredRegister< Item > ITEMS = DeferredRegister.create( ForgeRegistries.ITEMS, MajruszsDifficulty.MOD_ID );

	// Entities
	public static final RegistryObject< EntityType< GiantEntity > > GIANT = ENTITIES.register( "giant", ()->GiantEntity.type );
	public static final RegistryObject< EntityType< PillagerWolfEntity > > PILLAGER_WOLF = ENTITIES.register( "pillager_wolf",
		()->PillagerWolfEntity.type
	);
	public static final RegistryObject< EntityType< EliteSkeletonEntity > > ELITE_SKELETON = ENTITIES.register( "elite_skeleton",
		()->EliteSkeletonEntity.type
	);

	public static void init() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get()
			.getModEventBus();

		registerObjects( modEventBus );
		modEventBus.addListener( RegistryHandler::setup );
		modEventBus.addListener( RegistryHandler::doClientSetup );

		MinecraftForge.EVENT_BUS.addListener( DataSaver::onLoadingWorld );
		MinecraftForge.EVENT_BUS.addListener( DataSaver::onSavingWorld );
		MinecraftForge.EVENT_BUS.addListener( RegistryHandler::onServerStart );
	}

	private static void registerObjects( final IEventBus modEventBus ) {
		NewSpawnEggs.registerSpawnEgg( "giant_spawn_egg", GiantEntity.type, 44975, 7969893 );
		NewSpawnEggs.registerSpawnEgg( "pillager_wolf_spawn_egg", PillagerWolfEntity.type, 9804699, 5451574 );
		NewSpawnEggs.registerSpawnEgg( "illusioner_spawn_egg", EntityType.ILLUSIONER, 0x135a97, 9804699 );
		NewSpawnEggs.registerSpawnEgg( "elite_skeleton_spawn_egg", EliteSkeletonEntity.type, 0x135a97, 9804699 );

		ENTITIES.register( modEventBus );
		ITEMS.register( modEventBus );
	}

	private static void setup( final FMLCommonSetupEvent event ) {
		GlobalEntityTypeAttributes.put( GiantEntity.type, GiantEntity.getAttributeMap() );
		GlobalEntityTypeAttributes.put( PillagerWolfEntity.type, PillagerWolfEntity.getAttributeMap() );
		GlobalEntityTypeAttributes.put( EliteSkeletonEntity.type, EliteSkeletonEntity.getAttributeMap() );

		NewSpawnEggs.addDispenseBehaviorToAllRegisteredEggs();
	}

	private static void doClientSetup( final FMLClientSetupEvent event ) {
		RegistryHandlerClient.setup();
	}

	private static void onServerStart( FMLServerStartingEvent event ) {
		MinecraftServer server = event.getServer();
		Commands commands = server.getCommandManager();
		CommandDispatcher< CommandSource > dispatcher = commands.getDispatcher();

		ChangeGameStateCommand.register( dispatcher );
	}
}
