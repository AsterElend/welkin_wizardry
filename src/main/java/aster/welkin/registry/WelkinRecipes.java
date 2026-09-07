package aster.welkin.registry;

import aster.welkin.Welkin;
import aster.welkin.recipes.*;
import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class WelkinRecipes {
    public static RecipeType<AlchemyRecipe> ALCHEMY_TYPE;

  //  public static RecipeType<ForsakeRecipe> FORSAKE_TYPE;
 //   public static RecipeSerializer<ForsakeRecipe> FORSAKE_SERIALIZER;

     public static RecipeType<LightningRecipe> LIGHTNING_TYPE;
    public static RecipeSerializer<LightningRecipe> LIGHTNING_SERIALIZER;

    public static RecipeType<TeapotRecipe> TEAPOT_TYPE;
    public static RecipeSerializer<TeapotRecipe> TEAPOT_SERIALIZER;

    //public static RecipeType<ExtractorRecipe> EXTRACT_TYPE;
   // public static RecipeSerializer<ExtractorRecipe> EXTRACT_SERIALIZER;

    public static RecipeType<AgoniteTransmutationRecipe> AGONY_TYPE;
    public static RecipeSerializer<AgoniteTransmutationRecipe> AGONY_SERIALIZER;

    public static RecipeType<StormEyeRecipe> STORM_EYE_TYPE;
    public static RecipeSerializer<StormEyeRecipe> STORM_EYE_SERIALIZER;

    public static RecipeType<VoidRecipe> VOID_TYPE;
    public static RecipeSerializer<VoidRecipe> VOID_SERIALIZER;

    public static RecipeType<CaelumRecipe> CAELUM_TYPE;
    public static RecipeSerializer<CaelumRecipe> CAELUM_SERIALIZER;

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerSerializer(String id, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, Welkin.id(id), serializer);
    }

    static <T extends Recipe<?>> RecipeType<T> registerRecipeType(String id) {
        return Registry.register(Registries.RECIPE_TYPE, Welkin.id(id), new RecipeType<T>() {
            @Override
            public String toString() {
                return "welkin:" + id;
            }
        });
    }

    public static void register() {
        TEAPOT_TYPE = registerRecipeType("teapot");
        TEAPOT_SERIALIZER = registerSerializer("teapot", new TeapotRecipe.TeapotRecipeSerializer());



        //the cursed one; procedurally generated from custom reload listener
        ALCHEMY_TYPE = Registry.register(
                Registries.RECIPE_TYPE,
                Welkin.id("alchemy"),
                new RecipeType<AlchemyRecipe>() {
                    @Override
                    public String toString() {
                        return "welkin:alchemy";
                    }
                }
        );

        /*void brazier recipe
        FORSAKE_TYPE = Registry.register(
                Registries.RECIPE_TYPE,
                new Identifier("welkin", "forsake"),
                new RecipeType<ForsakeRecipe>() {
                    @Override
                    public String toString() {
                        return "welkin:forsake";
                    }
                }
        );

        FORSAKE_SERIALIZER = Registry.register(
                Registries.RECIPE_SERIALIZER,
                new Identifier("welkin", "forsake"),
                new ForsakeRecipe.ForsakeSerializer());
*/
        //smite recipe

        LIGHTNING_TYPE = Registry.register(
                Registries.RECIPE_TYPE,
                new Identifier("welkin", "lightning_transmutation"),
                new RecipeType<LightningRecipe>() {
                    @Override
                    public String toString() {
                        return "welkin:lightning_transmutation";
                    }
                }
        );

        LIGHTNING_SERIALIZER = Registry.register(
                Registries.RECIPE_SERIALIZER,
                new Identifier("welkin", "lightning_transmutation"),
                new ItemEntityTransmutationRecipe.Serializer<>(LightningRecipe::new));



        STORM_EYE_TYPE = Registry.register(
                Registries.RECIPE_TYPE,
                new Identifier("welkin", "storm_eye"),
                new RecipeType<StormEyeRecipe>() {
                    @Override
                    public String toString() {
                        return "welkin:storm_eye";
                    }
                }
        );

        STORM_EYE_SERIALIZER = Registry.register(
                Registries.RECIPE_SERIALIZER,
                new Identifier("welkin", "storm_eye"),
                new StormEyeRecipe.Serializer());


   VOID_TYPE = Registry.register(
                Registries.RECIPE_TYPE,
                new Identifier("welkin", "void_transmutation"),
                new RecipeType<VoidRecipe>() {
                    @Override
                    public String toString() {
                        return "welkin:void_transmutation";
                    }
                }
        );

        VOID_SERIALIZER = Registry.register(
                Registries.RECIPE_SERIALIZER,
                new Identifier("welkin", "void_transmutation"),
                new ItemEntityTransmutationRecipe.Serializer<>(VoidRecipe::new));



   CAELUM_TYPE = Registry.register(
                Registries.RECIPE_TYPE,
                new Identifier("welkin", "caelum_transmutation"),
                new RecipeType<CaelumRecipe>() {
                    @Override
                    public String toString() {
                        return "welkin:caelum_transmutation";
                    }
                }
        );

        CAELUM_SERIALIZER = Registry.register(
                Registries.RECIPE_SERIALIZER,
                new Identifier("welkin", "caelum_transmutation"),
                new ItemEntityTransmutationRecipe.Serializer<>(CaelumRecipe::new));



   AGONY_TYPE = Registry.register(
                Registries.RECIPE_TYPE,
                new Identifier("welkin", "agony_transmutation"),
                new RecipeType<AgoniteTransmutationRecipe>() {
                    @Override
                    public String toString() {
                        return "welkin:agony_transmutation";
                    }
                }
        );

        AGONY_SERIALIZER = Registry.register(
                Registries.RECIPE_SERIALIZER,
                new Identifier("welkin", "agony_transmutation"),
                new AgoniteTransmutationRecipe.AgonySerializer());





    }
}
