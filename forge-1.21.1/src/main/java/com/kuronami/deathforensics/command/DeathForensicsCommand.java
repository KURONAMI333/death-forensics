package com.kuronami.deathforensics.command;

import com.kuronami.deathforensics.death.DeathListener;
import com.kuronami.deathforensics.death.DeathRecord;
import com.kuronami.deathforensics.death.DeathReport;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * {@code /howdididie} (alias {@code /deathreport}).
 *
 * <p>No-arg form: any player reads their own last death. {@code
 * /howdididie <player>} is op-only (permission 2) so an admin can debug
 * "what keeps killing this person". Output is all
 * {@link Component#translatable} so it renders in each reader's own
 * language.
 */
public class DeathForensicsCommand {

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> d = event.getDispatcher();
        LiteralCommandNode<CommandSourceStack> node = d.register(
            Commands.literal("howdididie")
                .executes(this::self)
                .then(Commands.argument("player", EntityArgument.player())
                    .requires(src -> src.hasPermission(2))
                    .executes(this::other)));
        d.register(Commands.literal("deathreport").redirect(node));
    }

    private int self(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        if (!(src.getEntity() instanceof ServerPlayer player)) {
            src.sendFailure(Component.translatable("deathforensics.playeronly"));
            return 0;
        }
        return report(src, DeathListener.lastDeath(player.getUUID()));
    }

    private int other(CommandContext<CommandSourceStack> ctx) {
        try {
            ServerPlayer target = EntityArgument.getPlayer(ctx, "player");
            return report(ctx.getSource(), DeathListener.lastDeath(target.getUUID()));
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
            ctx.getSource().sendFailure(
                Component.translatable("deathforensics.playeronly"));
            return 0;
        }
    }

    private int report(CommandSourceStack src, DeathRecord r) {
        if (r == null) {
            src.sendSuccess(() -> Component.translatable("deathforensics.none")
                .withStyle(ChatFormatting.GRAY), false);
            return Command.SINGLE_SUCCESS;
        }
        // Same renderer as the automatic on-death post, so the command
        // (re-view / op lookup of another player) looks identical.
        for (Component line : DeathReport.render(r)) {
            src.sendSuccess(() -> line, false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
