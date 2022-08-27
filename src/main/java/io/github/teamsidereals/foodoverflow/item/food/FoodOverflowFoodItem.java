package io.github.teamsidereals.foodoverflow.item.food;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/*  Food saturation and hunger/nutrition formula

    Food register will have 2 value nutrition and saturationMod
    Saturation = nutrition * saturationMod * 2
    Example:
        Apple: nutrition = 4 ; saturationMod = 0.3 ; saturation = 4 * 0.3 * 2 = 2.4

    Note: Everything is point, 2 points = 1 bar. Player max nutrition and saturation value is 20 (10 bar)

 */
public class FoodOverflowFoodItem extends Item {

    public boolean isSavory;
    public boolean isSweet;
    public boolean isHealthy;
    public boolean isBland;
    public boolean isEffectModified;
    public int DamageResTimeModify;
    public int DamageResLevelModify;
    public int AbsorptionTimeModify;
    public int AbsorptionLevelModify;
    public int SpeedDownTimeModify;
    public int SpeedDownLevelModify;
    public int HasteTimeModify;
    public int HasteLevelModify;
    public int SpeedUpTimeModify;
    public int SpeedUpLevelModify;
    public int RegenerationTimeModify;
    public int RegenerationLevelModify;
    public int NightVisionTimeModify;
    public int NightVisionLevelModify;


    public FoodOverflowFoodItem(Properties properties) {
        super(properties);
        this.isSweet = false;
        this.isSavory = false;
        this.isHealthy = false;
        this.isBland = false;
        this.isEffectModified = false;
        this.DamageResTimeModify = 0;
        this.DamageResLevelModify = 0;
        this.AbsorptionTimeModify = 0;
        this.AbsorptionLevelModify = 0;
        this.SpeedDownTimeModify = 0;
        this.SpeedDownLevelModify = 0;
        this.HasteTimeModify = 0;
        this.HasteLevelModify = 0;
        this.SpeedUpTimeModify = 0;
        this.SpeedUpLevelModify = 0;
        this.RegenerationTimeModify = 0;
        this.RegenerationLevelModify = 0;
        this.NightVisionTimeModify = 0;
        this.NightVisionLevelModify = 0;
    }

    public FoodOverflowFoodItem alsoSavory(){
        this.isSavory = true;
        return this;
    }
    public FoodOverflowFoodItem alsoSweet(){
        this.isSweet = true;
        return this;
    }
    public FoodOverflowFoodItem alsoHealthy(){
        this.isHealthy = true;
        return this;
    }
    public FoodOverflowFoodItem alsoBland(){
        this.isBland = true;
        return this;
    }

    public FoodOverflowFoodItem modifyDamageRes(int time, int level){
        this.DamageResTimeModify = time;
        this.DamageResLevelModify = level;
        this.isEffectModified = true;
        return this;
    }

    public FoodOverflowFoodItem modifyAbsorption(int time, int level){
        this.AbsorptionTimeModify = time;
        this.AbsorptionLevelModify = level;
        this.isEffectModified = true;
        return this;
    }

    public FoodOverflowFoodItem modifySpeeddown(int time, int level){
        this.SpeedDownTimeModify = time;
        this.SpeedDownLevelModify = level;
        this.isEffectModified = true;
        return this;
    }

    public FoodOverflowFoodItem modifyHaste(int time, int level){
        this.HasteTimeModify = time;
        this.HasteLevelModify = level;
        this.isEffectModified = true;
        return this;
    }

    public FoodOverflowFoodItem modifySpeedUp(int time, int level){
        this.SpeedUpTimeModify = time;
        this.SpeedUpLevelModify = level;
        this.isEffectModified = true;
        return this;
    }

    public FoodOverflowFoodItem modifyRegeneration(int time, int level){
        this.RegenerationTimeModify = time;
        this.RegenerationLevelModify = level;
        this.isEffectModified = true;
        return this;
    }

    public FoodOverflowFoodItem modifyNightVision(int time, int level){
        this.NightVisionTimeModify = time;
        this.NightVisionLevelModify = level;
        this.isEffectModified = true;
        return this;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        if (((FoodOverflowFoodItem) stack.getItem()).isSavory){
            tooltip.add(new TranslationTextComponent(
                    "\u00A7eSavory\u00A7r"
                )
            );
        }
        if (((FoodOverflowFoodItem) stack.getItem()).isSweet){
            tooltip.add(new TranslationTextComponent(
                            "\u00A7dSweet\u00A7r"
                    )
            );
        }
        if (((FoodOverflowFoodItem) stack.getItem()).isHealthy){
            tooltip.add(new TranslationTextComponent(
                            "\u00A7aHealthy\u00A7r"
                    )
            );
        }
        if (((FoodOverflowFoodItem) stack.getItem()).isBland){
            tooltip.add(new TranslationTextComponent(
                            "\u00A7fBland\u00A7r"
                    )
            );
        }
        if (((FoodOverflowFoodItem) stack.getItem()).isEffectModified){
            tooltip.add(new TranslationTextComponent(
                            ""
                    )
            );
            tooltip.add(new TranslationTextComponent(
                            "\u00A7b-Eating Effect Modify-\u00A7r"
                    )
            );
            if (!Screen.hasShiftDown()){
                tooltip.add(new TranslationTextComponent(
                                "Hold \u00A7eSHIFT\u00A7r for details"
                        )
                );
            }
            else {
                if (((FoodOverflowFoodItem) stack.getItem()).DamageResTimeModify != 0
                        || ((FoodOverflowFoodItem) stack.getItem()).DamageResLevelModify != 0){
                    int time = ((FoodOverflowFoodItem) stack.getItem()).DamageResTimeModify;
                    int level = ((FoodOverflowFoodItem) stack.getItem()).DamageResLevelModify;
                    tooltip.add(new TranslationTextComponent(
                                    "Resistance: "
                                            + (time == 0 ? "" : time > 0 ? "\u00A7a+" + time/20 + "s" : "\u00A7c" + time/20 + "s")
                                            + "\u00A7r"
                                            + (((time != 0) && (level != 0)) ? " : " : "")
                                            + (level == 0 ? "" : "\u00A7d+" + level + "lv")
                                            + "\u00A7r"
                            )
                    );
                }
                if (((FoodOverflowFoodItem) stack.getItem()).AbsorptionTimeModify != 0
                        || ((FoodOverflowFoodItem) stack.getItem()).AbsorptionLevelModify != 0){
                    int time = ((FoodOverflowFoodItem) stack.getItem()).AbsorptionTimeModify;
                    int level = ((FoodOverflowFoodItem) stack.getItem()).AbsorptionLevelModify;
                    tooltip.add(new TranslationTextComponent(
                                    "Absorption: "
                                            + (time == 0 ? "" : time > 0 ? "\u00A7a+" + time/20 + "s" : "\u00A7c" + time/20 + "s")
                                            + "\u00A7r"
                                            + (((time != 0) && (level != 0)) ? " : " : "")
                                            + (level == 0 ? "" : "\u00A7d+" + level + "lv")
                                            + "\u00A7r"
                            )
                    );
                }
                if (((FoodOverflowFoodItem) stack.getItem()).SpeedDownTimeModify != 0
                        || ((FoodOverflowFoodItem) stack.getItem()).SpeedDownLevelModify != 0){
                    int time = ((FoodOverflowFoodItem) stack.getItem()).SpeedDownTimeModify;
                    int level = ((FoodOverflowFoodItem) stack.getItem()).SpeedDownLevelModify;
                    tooltip.add(new TranslationTextComponent(
                                    "Slowness: "
                                            + (time == 0 ? "" : time > 0 ? "\u00A7a+" + time/20 + "s" : "\u00A7c" + time/20 + "s")
                                            + "\u00A7r"
                                            + (((time != 0) && (level != 0)) ? " : " : "")
                                            + (level == 0 ? "" : "\u00A7d+" + level + "lv")
                                            + "\u00A7r"
                            )
                    );
                }
                if (((FoodOverflowFoodItem) stack.getItem()).HasteTimeModify != 0
                        || ((FoodOverflowFoodItem) stack.getItem()).HasteLevelModify != 0){
                    int time = ((FoodOverflowFoodItem) stack.getItem()).HasteTimeModify;
                    int level = ((FoodOverflowFoodItem) stack.getItem()).HasteLevelModify;
                    tooltip.add(new TranslationTextComponent(
                                    "Haste: "
                                            + (time == 0 ? "" : time > 0 ? "\u00A7a+" + time/20 + "s" : "\u00A7c" + time/20 + "s")
                                            + "\u00A7r"
                                            + (((time != 0) && (level != 0)) ? " : " : "")
                                            + (level == 0 ? "" : "\u00A7d+" + level + "lv")
                                            + "\u00A7r"
                            )
                    );
                }
                if (((FoodOverflowFoodItem) stack.getItem()).SpeedUpTimeModify != 0
                        || ((FoodOverflowFoodItem) stack.getItem()).SpeedUpLevelModify != 0){
                    int time = ((FoodOverflowFoodItem) stack.getItem()).SpeedUpTimeModify;
                    int level = ((FoodOverflowFoodItem) stack.getItem()).SpeedUpLevelModify;
                    tooltip.add(new TranslationTextComponent(
                                    "Speed: "
                                            + (time == 0 ? "" : time > 0 ? "\u00A7a+" + time/20 + "s" : "\u00A7c" + time/20 + "s")
                                            + "\u00A7r"
                                            + (((time != 0) && (level != 0)) ? " : " : "")
                                            + (level == 0 ? "" : "\u00A7d+" + level + "lv")
                                            + "\u00A7r"
                            )
                    );
                }
                if (((FoodOverflowFoodItem) stack.getItem()).RegenerationTimeModify != 0
                        || ((FoodOverflowFoodItem) stack.getItem()).RegenerationLevelModify != 0){
                    int time = ((FoodOverflowFoodItem) stack.getItem()).RegenerationTimeModify;
                    int level = ((FoodOverflowFoodItem) stack.getItem()).RegenerationLevelModify;
                    tooltip.add(new TranslationTextComponent(
                                    "Regeneration: "
                                            + (time == 0 ? "" : time > 0 ? "\u00A7a+" + time/20 + "s" : "\u00A7c" + time/20 + "s")
                                            + "\u00A7r"
                                            + (((time != 0) && (level != 0)) ? " : " : "")
                                            + (level == 0 ? "" : "\u00A7d+" + level + "lv")
                                            + "\u00A7r"
                            )
                    );
                }
                if (((FoodOverflowFoodItem) stack.getItem()).NightVisionTimeModify != 0
                        || ((FoodOverflowFoodItem) stack.getItem()).NightVisionLevelModify != 0){
                    int time = ((FoodOverflowFoodItem) stack.getItem()).NightVisionTimeModify;
                    int level = ((FoodOverflowFoodItem) stack.getItem()).NightVisionLevelModify;
                    tooltip.add(new TranslationTextComponent(
                                    "Night Vision: "
                                            + (time == 0 ? "" : time > 0 ? "\u00A7a+" + time/20 + "s" : "\u00A7c" + time/20 + "s")
                                            + "\u00A7r"
                                            + (((time != 0) && (level != 0)) ? " : " : "")
                                            + (level == 0 ? "" : "\u00A7d+" + level + "lv")
                                            + "\u00A7r"
                            )
                    );
                }
            }
        }
    }
}
