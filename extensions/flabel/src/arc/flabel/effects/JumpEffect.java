package arc.flabel.effects;

import arc.flabel.*;
import arc.math.*;

/** Makes the text jumps and falls as if there was gravity. */
public class JumpEffect extends FEffect{
    private static final float DEFAULT_FREQUENCY = 50f;
    private static final float DEFAULT_DISTANCE = 1.33f;
    private static final float DEFAULT_INTENSITY = 1f;

    private float distance = 1; // How much of their height they should move
    private float frequency = 1; // How frequently the wave pattern repeats
    private float intensity = 1; // How fast the glyphs should move

    public JumpEffect(FLabel label){
        super(label);
    }

    @Override
    protected void onApply(FGlyph glyph, int localIndex, float delta){
        // Calculate progress
        float progressModifier = (1f / intensity) * DEFAULT_INTENSITY;
        float normalFrequency = (1f / frequency) * DEFAULT_FREQUENCY;
        float progressOffset = localIndex / normalFrequency;
        float progress = calculateProgress(progressModifier, -progressOffset, false);

        // Calculate offset
        float interpolation = 0;
        float split = 0.2f;
        if(progress < split){
            interpolation = Interp.pow2Out.apply(0, 1, progress / split);
        }else{
            interpolation = Interp.bounceOut.apply(1, 0, (progress - split) / (1f - split));
        }
        float y = getLineHeight() * distance * interpolation * DEFAULT_DISTANCE;

        // Calculate fadeout
        float fadeout = calculateFadeout();
        y *= fadeout;

        // Apply changes
        glyph.yoffset += y;
    }

}