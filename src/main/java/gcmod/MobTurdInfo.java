package gcmod;

import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.monster.hoglin.Hoglin;

public class MobTurdInfo
{
    public int minInterval;
    public int maxInterval;

    private MobTurdInfo( int min, int max )
    {
        this.minInterval = min;
        this.maxInterval = max;
    }

    private static final MobTurdInfo COW_INFO = new MobTurdInfo( 600, 1800 );
    private static final MobTurdInfo HORSE_INFO = new MobTurdInfo( 600, 600 );
    private static final MobTurdInfo PIG_INFO = new MobTurdInfo( 300, 1800 );
    private static final MobTurdInfo SHEEP_INFO = new MobTurdInfo( 800, 1337 );

    public static MobTurdInfo forClass( Object animal )
    {
        assert animal != null;
        if ( animal instanceof Cow )
            return COW_INFO;
        if ( animal instanceof Horse )
            return HORSE_INFO;
        if ( animal instanceof Pig || animal instanceof Hoglin )
            return PIG_INFO;
        if ( animal instanceof Sheep || animal instanceof Goat )
            return SHEEP_INFO;

        return null;
    }
}
