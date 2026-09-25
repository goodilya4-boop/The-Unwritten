package com.theunwritten.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.theunwritten.TheUnwritten;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.BakedModelWrapper;

@EventBusSubscriber(
        modid = TheUnwritten.MODID,
        bus = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT)
public final class ElementalTreatiseClient {
    private static final ResourceLocation THREE_D_MODEL =
            ResourceLocation.fromNamespaceAndPath(
                    TheUnwritten.MODID,
                    "item/elemental_treatise_3d");

    private static final ModelResourceLocation THREE_D_MODEL_LOCATION =
            ModelResourceLocation.standalone(THREE_D_MODEL);

    private static final ModelResourceLocation ITEM_MODEL_LOCATION =
            ModelResourceLocation.inventory(
                    ResourceLocation.fromNamespaceAndPath(
                            TheUnwritten.MODID,
                            "elemental_treatise"));

    private ElementalTreatiseClient() {
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(THREE_D_MODEL_LOCATION);
    }

    @SubscribeEvent
    public static void modifyBakingResult(ModelEvent.ModifyBakingResult event) {
        BakedModel inventoryModel = event.getModels().get(ITEM_MODEL_LOCATION);
        BakedModel threeDModel = event.getModels().get(THREE_D_MODEL_LOCATION);

        if (inventoryModel == null || threeDModel == null) {
            return;
        }

        event.getModels().put(
                ITEM_MODEL_LOCATION,
                new ElementalTreatiseBakedModel(inventoryModel, threeDModel));
    }

    private static final class ElementalTreatiseBakedModel
            extends BakedModelWrapper<BakedModel> {
        private final BakedModel threeDModel;

        private ElementalTreatiseBakedModel(
                BakedModel inventoryModel,
                BakedModel threeDModel) {
            super(inventoryModel);
            this.threeDModel = threeDModel;
        }

        @Override
        public BakedModel applyTransform(
                ItemDisplayContext context,
                PoseStack poseStack,
                boolean applyLeftHandTransform) {
            if (context == ItemDisplayContext.GUI) {
                return ClientHooks.handleCameraTransforms(
                        poseStack,
                        this.originalModel,
                        context,
                        applyLeftHandTransform);
            }

            return ClientHooks.handleCameraTransforms(
                    poseStack,
                    this.threeDModel,
                    context,
                    applyLeftHandTransform);
        }
    }
}
