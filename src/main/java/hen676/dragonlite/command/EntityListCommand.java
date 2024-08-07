package hen676.dragonlite.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class EntityListCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("list_entities")
                .then(ClientCommandManager.argument("radius", IntegerArgumentType.integer(0,128)))
                    .executes(context -> listEntitiesAroundPlayer(context.getSource(), IntegerArgumentType.getInteger(context, "radius"))));
    }

    // TODO:: fix
    public static int listEntitiesAroundPlayer(FabricClientCommandSource source, int radius) {
        ClientPlayerEntity player = source.getClient().player;
        ClientWorld world = source.getClient().world;
        if (world == null || player == null) return 0;

        BlockPos playerPos = player.getBlockPos();

        Map<Text, Integer> entityNames = new HashMap<>();

        world.getEntities().forEach((entity -> {
            if (entity.isPlayer()) return;
            if (entity.getBlockPos().isWithinDistance(playerPos,radius)) {
                Text name = entity.getName();
                if (entityNames.containsKey(name))
                    entityNames.put(name, entityNames.get(name) + 1);
                else
                    entityNames.put(name, 1);
            }
        }));

        if (entityNames.isEmpty()) {
            source.sendFeedback(Text.literal("No entities nearby!"));
            return 1;
        }


        source.sendFeedback(Text.literal("List of entities from %s distance from player".formatted(radius)));
        entityNames.forEach((text, amount) -> source.sendFeedback(text.copy().append(" - ").append(String.valueOf(amount))));
        return 1;
    }
}
