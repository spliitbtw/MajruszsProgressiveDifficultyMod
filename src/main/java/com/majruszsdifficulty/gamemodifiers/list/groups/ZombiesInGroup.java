package com.majruszsdifficulty.gamemodifiers.list.groups;

import com.majruszsdifficulty.GameStage;
import com.majruszsdifficulty.Registries;
import com.majruszsdifficulty.gamemodifiers.CustomConditions;
import com.majruszsdifficulty.gamemodifiers.configs.MobGroupConfig;
import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnSpawned;
import com.mlib.levels.LevelHelper;
import com.mlib.math.Range;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;

@AutoInstance
public class ZombiesInGroup extends GameModifier {
	final MobGroupConfig mobGroups = new MobGroupConfig(
		()->EntityType.ZOMBIE,
		new Range<>( 1, 3 ),
		Registries.getLocation( "mob_groups/zombie_leader" ),
		Registries.getLocation( "mob_groups/zombie_sidekick" )
	);

	public ZombiesInGroup() {
		super( Registries.Modifiers.DEFAULT );

		new OnSpawned.ContextSafe( this::spawnGroup )
			.addCondition( new CustomConditions.GameStage<>( GameStage.EXPERT ) )
			.addCondition( new CustomConditions.CRDChance<>( 0.25, true ) )
			.addCondition( new CustomConditions.IsNotPartOfGroup<>() )
			.addCondition( new CustomConditions.IsNotUndeadArmy<>() )
			.addCondition( new Condition.IsServer<>() )
			.addCondition( new Condition.Excludable<>() )
			.addCondition( new OnSpawned.IsNotLoadedFromDisk<>() )
			.addCondition( new OnSpawned.Is<>( Zombie.class ) )
			.addCondition( data->!LevelHelper.isEntityOutside( data.target ) && data.target.position().y < 50.0f )
			.addConfig( this.mobGroups.name( "Zombies" ) )
			.insertTo( this );

		this.name( "ZombiesInGroup" ).comment( "Zombies may spawn in groups as miners (only underground)." );
	}

	private void spawnGroup( OnSpawned.Data data ) {
		this.mobGroups.spawn( ( PathfinderMob )data.target );
	}
}
