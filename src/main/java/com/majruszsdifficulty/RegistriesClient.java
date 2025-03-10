package com.majruszsdifficulty;

import com.majruszsdifficulty.entities.CerberusEntity;
import com.majruszsdifficulty.items.EndShardLocatorItem;
import com.majruszsdifficulty.models.*;
import com.majruszsdifficulty.renderers.*;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;

@Deprecated( forRemoval = true )
@OnlyIn( Dist.CLIENT )
public class RegistriesClient {
	public static void setup() {
		ForgeHooksClient.registerLayerDefinition( CreeperlingRenderer.LAYER, ()->CreeperlingModel.createBodyLayer( CubeDeformation.NONE ) );
		ForgeHooksClient.registerLayerDefinition( TankRenderer.LAYER, ()->TankModel.createBodyLayer( CubeDeformation.NONE ) );
		ForgeHooksClient.registerLayerDefinition( BlackWidowRenderer.LAYER, BlackWidowModel::createBodyLayer );
		ForgeHooksClient.registerLayerDefinition( CursedArmorRenderer.INNER_ARMOR_LAYER, ()->LayerDefinition.create( CursedArmorModel.createMesh( LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0f ), 64, 32 ) );
		ForgeHooksClient.registerLayerDefinition( CursedArmorRenderer.OUTER_ARMOR_LAYER, ()->LayerDefinition.create( CursedArmorModel.createMesh( LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0f ), 64, 32 ) );
		ForgeHooksClient.registerLayerDefinition( CursedArmorRenderer.MAIN_LAYER, ()->LayerDefinition.create( CursedArmorModel.createMesh( CubeDeformation.NONE, 0.0f ), 64, 64 ) );
		ForgeHooksClient.registerLayerDefinition( CerberusRenderer.LAYER, CerberusModel::createBodyLayer );

		EntityRenderers.register( Registries.CREEPERLING.get(), CreeperlingRenderer::new );
		EntityRenderers.register( Registries.TANK.get(), TankRenderer::new );
		EntityRenderers.register( Registries.BLACK_WIDOW.get(), BlackWidowRenderer::new );
		EntityRenderers.register( Registries.CURSED_ARMOR.get(), CursedArmorRenderer::new );
		EntityRenderers.register( Registries.CERBERUS.get(), CerberusRenderer::new );

		ItemProperties.register( Registries.ENDERIUM_SHARD_LOCATOR.get(), new ResourceLocation( "shard_distance" ), EndShardLocatorItem::calculateDistanceToEndShard );
	}
}
