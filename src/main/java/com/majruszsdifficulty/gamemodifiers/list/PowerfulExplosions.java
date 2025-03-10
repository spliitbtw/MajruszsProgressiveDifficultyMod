package com.majruszsdifficulty.gamemodifiers.list;

import com.majruszsdifficulty.GameStage;
import com.majruszsdifficulty.Registries;
import com.mlib.Random;
import com.mlib.annotations.AutoInstance;
import com.mlib.config.DoubleConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnExplosion;
import com.mlib.math.Range;
import net.minecraftforge.event.level.ExplosionEvent;

@AutoInstance
public class PowerfulExplosions extends GameModifier {
	final DoubleConfig radiusMultiplier = new DoubleConfig( 1.2599, new Range<>( 1.0, 10.0 ) );
	final DoubleConfig fireChance = new DoubleConfig( 0.75, Range.CHANCE );

	public PowerfulExplosions() {
		super( Registries.Modifiers.DEFAULT );

		new OnExplosion.Context( this::modifyExplosion )
			.addCondition( new Condition.Excludable<>() )
			.addCondition( data->data.level != null )
			.addCondition( data->data.event instanceof ExplosionEvent.Start )
			.addConfigs( this.radiusMultiplier
				.name( "radius_multiplier" )
				.comment( "Multiplies explosion radius by the given value (this value is scaled by Clamped Regional Difficulty)." )
			).addConfigs( this.fireChance
				.name( "fire_chance" )
				.comment( "Gives all explosions a chance to cause fire (this value is scaled by Clamped Regional Difficulty)." )
			).insertTo( this );

		this.name( "PowerfulExplosions" ).comment( "Makes all explosions (creepers, ghast ball etc.) much more deadly." );
	}

	private void modifyExplosion( OnExplosion.Data data ) {
		double clampedRegionalDifficulty = GameStage.getRegionalDifficulty( data.level, data.explosion.getPosition() );
		double radiusMultiplier = clampedRegionalDifficulty * ( this.radiusMultiplier.get() - 1.0 ) + 1.0;
		data.radius.setValue( radiusMultiplier * data.radius.getValue() );
		if( Random.tryChance( clampedRegionalDifficulty * this.fireChance.get() ) ) {
			data.causesFire.setValue( true );
		}
	}
}
