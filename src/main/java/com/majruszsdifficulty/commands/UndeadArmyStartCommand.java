package com.majruszsdifficulty.commands;

import com.majruszsdifficulty.Registries;
import com.majruszsdifficulty.undeadarmy.data.Direction;
import com.mlib.annotations.AutoInstance;
import com.mlib.commands.CommandData;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

@AutoInstance
public class UndeadArmyStartCommand extends DifficultyCommand {
	public UndeadArmyStartCommand() {
		this.newBuilder()
			.literal( "undeadarmy" )
			.literal( "start" )
			.hasPermission( 4 )
			.execute( this::handle )
			.enumeration( Direction.class )
			.execute( this::handle )
			.entity()
			.execute( this::handle );
	}

	private int handle( CommandData data ) throws CommandSyntaxException {
		Optional< Direction > direction = this.getOptionalEnumeration( data, Direction.class );
		Vec3 position = this.getOptionalEntityOrPlayer( data ).position();
		if( Registries.getUndeadArmyManager().tryToSpawn( new BlockPos( position ), direction ) ) {
			data.source.sendSuccess( Component.translatable( "commands.undeadarmy.started", asVec3i( position ) ), true );
			return 0;
		}

		data.source.sendSuccess( Component.translatable( "commands.undeadarmy.cannot_start", asVec3i( position ) ), true );
		return -1;
	}
}
