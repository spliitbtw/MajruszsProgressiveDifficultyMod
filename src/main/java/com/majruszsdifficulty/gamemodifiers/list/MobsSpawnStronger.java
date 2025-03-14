package com.majruszsdifficulty.gamemodifiers.list;

import com.majruszsdifficulty.Registries;
import com.majruszsdifficulty.config.GameStageDoubleConfig;
import com.mlib.Utility;
import com.mlib.annotations.AutoInstance;
import com.mlib.attributes.AttributeHandler;
import com.mlib.config.StringListConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnSpawned;
import com.mlib.math.Range;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

@AutoInstance
public class MobsSpawnStronger extends GameModifier {
	static final AttributeHandler MAX_HEALTH_ATTRIBUTE = new AttributeHandler( "ba9de909-4a9e-43da-9d14-fbcbc2403316", "ProgressiveDifficultyHealthBonus", Attributes.MAX_HEALTH, AttributeModifier.Operation.MULTIPLY_BASE );
	static final AttributeHandler DAMAGE_ATTRIBUTE = new AttributeHandler( "053d92c8-ccb5-4b95-9add-c31aca144177", "ProgressiveDifficultyDamageBonus", Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE );
	final GameStageDoubleConfig healthBonus = new GameStageDoubleConfig( 0.0, 0.15, 0.3, new Range<>( 0.0, 10.0 ) );
	final GameStageDoubleConfig damageBonus = new GameStageDoubleConfig( 0.0, 0.15, 0.3, new Range<>( 0.0, 10.0 ) );
	final GameStageDoubleConfig nightMultiplier = new GameStageDoubleConfig( 2.0, 2.0, 2.0, new Range<>( 1.0, 10.0 ) );
	final StringListConfig excludedMobs = new StringListConfig();
	final StringListConfig excludedDimensions = new StringListConfig();

	public MobsSpawnStronger() {
		super( Registries.Modifiers.DEFAULT );

		new OnSpawned.Context( this::makeMobsStronger )
			.addCondition( new Condition.IsServer<>() )
			.addCondition( new Condition.Excludable<>() )
			.addCondition( new OnSpawned.IsNotLoadedFromDisk<>() )
			.addCondition( data->this.canMobAttack( data.target ) )
			.addCondition( data->this.isNotDimensionExcluded( data.level ) )
			.addCondition( data->this.isNotMobExcluded( data.target ) )
			.addConfigs( this.healthBonus.name( "HealthBonusMultiplier" ) )
			.addConfigs( this.damageBonus.name( "DamageBonusMultiplier" ) )
			.addConfigs( this.nightMultiplier.name( "NightMultiplier" ).comment( "Multiplies health and damage bonuses at night." ) )
			.addConfigs( this.excludedMobs.name( "excluded_mobs" )
				.comment( "List of mobs that should not get health and damage bonuses. (for instance minecraft:wither)" )
			).addConfigs( this.excludedDimensions.name( "excluded_dimensions" )
				.comment( "List of dimensions where health and damage bonuses should not be applied. (for instance minecraft:overworld)" )
			).insertTo( this );

		this.name( "MobsSpawnStronger" ).comment( "All hostile mobs get damage and health bonuses." );
	}

	private void makeMobsStronger( OnSpawned.Data data ) {
		assert data.level != null;
		LivingEntity entity = data.target;
		double nightMultiplier = data.level.isNight() ? this.nightMultiplier.get() : 1.0;

		MAX_HEALTH_ATTRIBUTE.setValueAndApply( entity, this.healthBonus.getCurrentGameStageValue() * nightMultiplier );
		DAMAGE_ATTRIBUTE.setValueAndApply( entity, this.damageBonus.getCurrentGameStageValue() * nightMultiplier );
		entity.setHealth( entity.getMaxHealth() );
	}

	private boolean canMobAttack( LivingEntity entity ) {
		return entity instanceof Mob && AttributeHandler.hasAttribute( entity, Attributes.ATTACK_DAMAGE );
	}

	private boolean isNotDimensionExcluded( ServerLevel level ) {
		return !this.excludedDimensions.contains( Utility.getRegistryString( level ) );
	}

	private boolean isNotMobExcluded( LivingEntity entity ) {
		return !this.excludedMobs.contains( Utility.getRegistryString( entity ) );
	}
}
