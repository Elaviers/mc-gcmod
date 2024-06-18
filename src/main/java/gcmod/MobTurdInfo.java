package gcmod;

import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.passive.*;

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
        if ( animal instanceof CowEntity )
            return COW_INFO;
        if ( animal instanceof HorseEntity )
            return HORSE_INFO;
        if ( animal instanceof PigEntity || animal instanceof HoglinEntity )
            return PIG_INFO;
        if ( animal instanceof SheepEntity || animal instanceof GoatEntity )
            return SHEEP_INFO;

        return null;
    }
}
