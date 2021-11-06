package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.common.tile.TileDetector;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.common.ToolType;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockDetector extends Block {
    private static final VoxelShape shape = Shapes.box(0, 0, 0, 1, 11.0 / 16.0, 1);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public BlockDetector() {
	super(Properties.of(Material.METAL).strength(3.5F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).noOcclusion());
	registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    @Deprecated
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
	return shape;
    }

    @Override
    @Deprecated
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
	return Arrays.asList(new ItemStack(this));
    }

    @Override
    @Deprecated
    public BlockState rotate(BlockState state, Rotation rot) {
	return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    @Deprecated
    public boolean isSignalSource(BlockState state) {
	return true;
    }

    @Override
    @Deprecated
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
	return blockState.getSignal(blockAccess, pos, side);
    }

    @Override
    @Deprecated
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
	BlockEntity tile = blockAccess.getBlockEntity(pos);
	if (tile instanceof TileDetector) {
	    return ((TileDetector) tile).isPowered ? 15 : 0;
	}
	return 0;
    }

    @Deprecated
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
	return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
	return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
	builder.add(FACING);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
	return true;
    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
	return new TileDetector();
    }
}
