package com.plainprog.grandslam_ai.helper.image;

import com.plainprog.grandslam_ai.object.dto.image.ImageDTO;
import com.plainprog.grandslam_ai.object.constant.images.ImgGenModuleId;

import java.util.List;
import java.util.Arrays;

public class ModuleExamples {

    public static final class ExampleEntry {
        public String prompt;
        public ImageDTO image;

        public ExampleEntry() {
        }

        public ExampleEntry(String prompt, ImageDTO image) {
            this.prompt = prompt;
            this.image = image;
        }
    }

    public static List<ExampleEntry> getExamplesByModuleId(int moduleId) {
        switch (moduleId) {
            case ImgGenModuleId.REALISTIC_PORTRAIT:
                return PORTRAIT_EXAMPLES;
            case ImgGenModuleId.OCTANE:
                return OCTANE_EXAMPLES;
            case ImgGenModuleId.ACID:
                return ACID_EXAMPLES;
            case ImgGenModuleId.DIGITAL_PHOTOGRAPHY:
                return DIGITAL_PHOTOGRAPHY_EXAMPLES;
            case ImgGenModuleId.PIXEL_ART:
                return PIXEL_ART_EXAMPLES;
            case ImgGenModuleId.VECTOR_ART:
                return VECTOR_ART_EXAMPLES;
            case ImgGenModuleId.LINE_ART:
                return LINE_ART_EXAMPLES;
            case ImgGenModuleId.AESTHETIC:
                return AESTHETIC_EXAMPLES;
            case ImgGenModuleId.MACRO:
                return MACRO_EXAMPLES;
            case ImgGenModuleId.IMPRESSIONISM_ART:
                return IMPRESSIONISM_ART_EXAMPLES;
            case ImgGenModuleId.REALISM_ART:
                return REALISM_ART_EXAMPLES;
            case ImgGenModuleId.EXPRESSIONISM_ART:
                return EXPRESSIONISM_ART_EXAMPLES;
            case ImgGenModuleId.SURREALISM_ART:
                return SURREALISM_ART_EXAMPLES;
            case ImgGenModuleId.ABSTRACT_ART:
                return ABSTRACT_ART_EXAMPLES;
            case ImgGenModuleId.CUBISM_ART:
                return CUBISM_ART_EXAMPLES;
            case ImgGenModuleId.ART_NOUVEAU_ART:
                return ART_NOUVEAU_ART_EXAMPLES;
            case ImgGenModuleId.GOTHIC_ART:
                return GOTHIC_ART_EXAMPLES;
            case ImgGenModuleId.DADAISM_ART:
                return DADAISM_ART_EXAMPLES;
            case ImgGenModuleId.NEOCLASSICISM_ART:
                return NEOCLASSICISM_ART_EXAMPLES;
            case ImgGenModuleId.ROMANTICISM_ART:
                return ROMANTICISM_ART_EXAMPLES;
            case ImgGenModuleId.POST_IMPRESSIONISM_ART:
                return POST_IMPRESSIONISM_ART_EXAMPLES;
            case ImgGenModuleId.NEBULA:
                return NEBULA_EXAMPLES;
            default:
                return List.of();
        }
    }


    public static final List<ExampleEntry> PORTRAIT_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Girl, curls, freckles, ginger hair, smiling with joy",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737050745929.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737050745929.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737050745929.jpg"
                    )
            ),
            new ExampleEntry(
                    "50 y.o asian man, leather jacket, dark clothes, serious face",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737050886779.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737050886779.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737050886779.jpg"
                    )
            ),
            new ExampleEntry(
                    "Woman in a public restroom, short hair, sleek black dress with an open back, graffiti-covered walls, dimly lit room, illuminated by a neon sign, casts a moody glow,red lips, intrigue eyes",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737051344715.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737051344715.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737051344715.jpg"
                    )
            ),
            new ExampleEntry(
                    "Caucasian man, black sweater, dramatic lighting, nature, gloomy, cloudy weather",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737051674564.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737051674564.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737051674564.jpg"
                    )
            )
    );

    public static final List<ExampleEntry> OCTANE_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Red e30 BMW M3 at sunset, snowy mountines background",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1736950889864.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1736950889864.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1736950889864.jpg"
                    )
            ),
            new ExampleEntry(
                    "Young woman sitting on a bench, black dress with a plunging neckline and long sleeves, long dark hair that black lace-up boots and thigh-high stockings with fishnet stockings, looking off to the side with a serious expression on her face, background of the image is filled with red roses",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1736950390545.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1736950390545.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1736950390545.jpg"
                    )
            ),
            new ExampleEntry(
                    "View from balcony, Paris , night, illuminated eiffel tower, futuristic, flowers, romantic evening",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1736950168076.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1736950168076.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1736950168076.jpg"
                    )
            ),
            new ExampleEntry(
                    "Ancient Korean palace silhouette, Gyeongbokgung architecture elements, subtle gold accents, mountain peaks in distance",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1736949639075.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1736949639075.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1736949639075.jpg"
                    )
            )
    );

    public static final List<ExampleEntry> MACRO_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Ladybird, very close, slight waterdrops",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737113794300.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737113794300.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737113794300.jpg"
                    )
            ),
            new ExampleEntry(
                    "Gray parrot",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737113153581.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737113153581.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737113153581.jpg"
                    )
            ),
            new ExampleEntry(
                    "Peacock butterfly, white flower",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737113401048.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737113401048.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737113401048.jpg"
                    )
            ),
            new ExampleEntry(
                    "Spider web, blurred background",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737114954924.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737114954924.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737114954924.jpg"
                    )
            )
    );

    public static final List<ExampleEntry> AESTHETIC_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Balcony with pillows, flowers and candle. Evening sunset seaview",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737128866400.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737128866400.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737128866400.jpg"
                    )
            ),
            new ExampleEntry(
                    "Old porsche in a showroom",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737128262320.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737128262320.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737128262320.jpg"
                    )
            ),
            new ExampleEntry(
                    "Red-white apartament with panoramic seaview windows",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737128004313.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737128004313.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737128004313.jpg"
                    )
            ),
            new ExampleEntry(
                    "Curly afroamerican woman, ancient Egyptian palace, antique luxury, blen of architecture and nature, green plants",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1738605579196.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1738605579196.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1738605579196.jpg"
                    )
            )
    );

    public static final List<ExampleEntry> ACID_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Whale floating in colorful space",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737834787513.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737834787513.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737834787513.jpg"
                    )
            ),
            new ExampleEntry(
                    "City viewpoint, landscape, sky, moon",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737834971258.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737834971258.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737834971258.jpg"
                    )
            ),
            new ExampleEntry(
                    "Cyborg girl, biomechanical details",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737835172551.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737835172551.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737835172551.jpg"
                    )
            ),
            new ExampleEntry(
                    "Giant jellyfish, underwater world",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737835327446.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737835327446.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737835327446.jpg"
                    )
            )
    );

    public static final List<ExampleEntry> DIGITAL_PHOTOGRAPHY_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Man in suit, standing in pool in water",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737841069935.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737841069935.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737841069935.jpg"
                    )
            ),
            new ExampleEntry(
                    "Female, abstract, red hair, mirrors",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737841153206.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737841153206.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737841153206.jpg"
                    )
            ),
            new ExampleEntry(
                    "Stairs, eternity, fog",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737843946761.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737843946761.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737843946761.jpg"
                    )
            ),
            new ExampleEntry(
                    "Blend of winter and summer, single tree",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737844007464.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737844007464.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737844007464.jpg"
                    )
            )
    );
    public static final List<ExampleEntry> PIXEL_ART_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Blue door and pink roses, stone masonry path, tidy old house entrance",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737844446827.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737844446827.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737844446827.jpg"
                    )
            ),
            new ExampleEntry(
                    "A day at sea beach, woman figure in white dress and white hat",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737844472291.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737844472291.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737844472291.jpg"
                    )
            ),
            new ExampleEntry(
                    "Night city, skyscrapers, neon lights",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737844521529.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737844521529.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737844521529.jpg"
                    )
            ),
            new ExampleEntry(
                    "Mona Lisa",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737844579174.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737844579174.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737844579174.jpg"
                    )
            )
    );
    public static final List<ExampleEntry> VECTOR_ART_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "A day at sea beach, woman figure in white dress and white hat",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737844805902.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737844805902.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737844805902.jpg"
                    )
            ),
            new ExampleEntry(
                    "Woman portrait, orange tones",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737844881102.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737844881102.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737844881102.jpg"
                    )
            ),
            new ExampleEntry(
                    "Green owl abstract, grey background",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737844934864.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737844934864.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737844934864.jpg"
                    )
            ),
            new ExampleEntry(
                    "Kilimanjaro with huge sun above it",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737844978141.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737844978141.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737844978141.jpg"
                    )
            )
    );
    public static final List<ExampleEntry> LINE_ART_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "A day at sea beach, woman figure in white dress and white hat",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737845168360.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737845168360.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737845168360.jpg"
                    )
            ),
            new ExampleEntry(
                    "Ryan Gosling portrait",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737845254691.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737845254691.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737845254691.jpg"
                    )
            ),
            new ExampleEntry(
                    "Owl",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737845197609.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737845197609.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737845197609.jpg"
                    )
            ),
            new ExampleEntry(
                    "Old Porsche 911",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737845423975.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737845423975.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737845423975.jpg"
                    )
            )
    );
    public static final List<ExampleEntry> IMPRESSIONISM_ART_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Wooden bench in a city park, people with umbrellas in rainy weather, rays of sun coming through clouds",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737659501181.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737659501181.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737659501181.jpg"
                    )
            ),
            new ExampleEntry(
                    "Still life, table with fruits, vases, wine and olive leaves",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664141769.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664141769.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664141769.jpg"
                    )
            ),
            new ExampleEntry(
                    "A day at sea beach, woman figure in white dress and white hat",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664813520.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664813520.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664813520.jpg"
                    )
            ),
            new ExampleEntry(
                    "Blue door and pink roses, stone masonry path, tidy old house entrance",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737665314731.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737665314731.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737665314731.jpg"
                    )
            )
    );

    public static final List<ExampleEntry> EXPRESSIONISM_ART_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Wooden bench in a city park, people with umbrellas in rainy weather, rays of sun coming through clouds",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737663415355.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737663415355.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737663415355.jpg"
                    )
            ),
            new ExampleEntry(
                    "Still life, table with fruits, vases, wine and olive leaves",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664156940.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664156940.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664156940.jpg"
                    )
            ),
            new ExampleEntry(
                    "A day at sea beach, woman figure in white dress and white hat",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664834928.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664834928.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664834928.jpg"
                    )
            ),
            new ExampleEntry(
                    "Blue door and pink roses, stone masonry path, tidy old house entrance",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737665331027.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737665331027.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737665331027.jpg"
                    )
            )
    );
    public static final List<ExampleEntry> REALISM_ART_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Wooden bench in a city park, people with umbrellas in rainy weather, rays of sun coming through clouds",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737663615360.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737663615360.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737663615360.jpg"
                    )
            ),
            new ExampleEntry(
                    "Still life, table with fruits, vases, wine and olive leaves",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664286880.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664286880.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664286880.jpg"
                    )
            ),
            new ExampleEntry(
                    "A day at sea beach, woman figure in white dress and white hat",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664944282.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664944282.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664944282.jpg"
                    )
            ),
            new ExampleEntry(
                    "Blue door and pink roses, stone masonry path, tidy old house entrance",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737665694656.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737665694656.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737665694656.jpg"
                    )
            )
    );
    public static final List<ExampleEntry> SURREALISM_ART_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Wooden bench in a city park, people with umbrellas in rainy weather, rays of sun coming through clouds",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737663451702.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737663451702.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737663451702.jpg"
                    )
            ),
            new ExampleEntry(
                    "Still life, table with fruits, vases, wine and olive leaves",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664196103.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664196103.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664196103.jpg"
                    )
            ),
            new ExampleEntry(
                    "A day at sea beach, woman figure in white dress and white hat",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664848769.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664848769.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664848769.jpg"
                    )
            ),
            new ExampleEntry(
                    "Blue door and pink roses, stone masonry path, tidy old house entrance",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737665433202.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737665433202.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737665433202.jpg"
                    )
            )
    );

    public static final List<ExampleEntry> ABSTRACT_ART_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Wooden bench in a city park, people with umbrellas in rainy weather, rays of sun coming through clouds",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737663499297.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737663499297.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737663499297.jpg"
                    )
            ),
            new ExampleEntry(
                    "Still life, table with fruits, vases, wine and olive leaves",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664225711.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664225711.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664225711.jpg"
                    )
            ),
            new ExampleEntry(
                    "A day at sea beach, woman figure in white dress and white hat",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664866624.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664866624.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664866624.jpg"
                    )
            ),
            new ExampleEntry(
                    "Blue door and pink roses, stone masonry path, tidy old house entrance",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737665463644.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737665463644.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737665463644.jpg"
                    )
            )
    );
    public static final List<ExampleEntry> CUBISM_ART_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Wooden bench in a city park, people with umbrellas in rainy weather, rays of sun coming through clouds",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737659614220.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737659614220.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737659614220.jpg"
                    )
            ),
            new ExampleEntry(
                    "Still life, table with fruits, vases, wine and olive leaves",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737662202080.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737662202080.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737662202080.jpg"
                    )
            ),
            new ExampleEntry(
                    "A day at sea beach, woman figure in white dress and white hat",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737662617036.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737662617036.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737662617036.jpg"
                    )
            ),
            new ExampleEntry(
                    "Blue door and pink roses, stone masonry path, tidy old house entrance",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737665616567.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737665616567.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737665616567.jpg"
                    )
            )
    );

    public static final List<ExampleEntry> ART_NOUVEAU_ART_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Wooden bench in a city park, people with umbrellas in rainy weather, rays of sun coming through clouds",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737663530849.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737663530849.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737663530849.jpg"
                    )
            ),
            new ExampleEntry(
                    "Still life, table with fruits, vases, wine and olive leaves",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664336865.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664336865.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664336865.jpg"
                    )
            ),
            new ExampleEntry(
                    "A day at sea beach, woman figure in white dress and white hat",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664890554.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664890554.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664890554.jpg"
                    )
            ),
            new ExampleEntry(
                    "Blue door and pink roses, stone masonry path, tidy old house entrance",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737665629842.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737665629842.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737665629842.jpg"
                    )
            )
    );
    public static final List<ExampleEntry> GOTHIC_ART_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Wooden bench in a city park, people with umbrellas in rainy weather, rays of sun coming through clouds",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737663588615.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737663588615.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737663588615.jpg"
                    )
            ),
            new ExampleEntry(
                    "Still life, table with fruits, vases, wine and olive leaves",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664372125.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664372125.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664372125.jpg"
                    )
            ),
            new ExampleEntry(
                    "A day at sea beach, woman figure in white dress and white hat",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664905205.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664905205.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664905205.jpg"
                    )
            ),
            new ExampleEntry(
                    "Blue door and pink roses, stone masonry path, tidy old house entrance",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737665654201.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737665654201.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737665654201.jpg"
                    )
            )
    );

    public static final List<ExampleEntry> DADAISM_ART_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Wooden bench in a city park, people with umbrellas in rainy weather, rays of sun coming through clouds",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737663663076.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737663663076.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737663663076.jpg"
                    )
            ),
            new ExampleEntry(
                    "Still life, table with fruits, vases, wine and olive leaves",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664399811.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664399811.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664399811.jpg"
                    )
            ),
            new ExampleEntry(
                    "A day at sea beach, woman figure in white dress and white hat",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664959907.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664959907.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664959907.jpg"
                    )
            ),
            new ExampleEntry(
                    "Blue door and pink roses, stone masonry path, tidy old house entrance",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737665749653.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737665749653.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737665749653.jpg"
                    )
            )
    );
    public static final List<ExampleEntry> NEOCLASSICISM_ART_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Wooden bench in a city park, people with umbrellas in rainy weather, rays of sun coming through clouds",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737663854894.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737663854894.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737663854894.jpg"
                    )
            ),
            new ExampleEntry(
                    "Still life, table with fruits, vases, wine and olive leaves",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664427482.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664427482.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664427482.jpg"
                    )
            ),
            new ExampleEntry(
                    "A day at sea beach, woman figure in white dress and white hat",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664983145.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664983145.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664983145.jpg"
                    )
            ),
            new ExampleEntry(
                    "Blue door and pink roses, stone masonry path, tidy old house entrance",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737665766800.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737665766800.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737665766800.jpg"
                    )
            )
    );

    public static final List<ExampleEntry> ROMANTICISM_ART_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Wooden bench in a city park, people with umbrellas in rainy weather, rays of sun coming through clouds",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737663811826.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737663811826.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737663811826.jpg"
                    )
            ),
            new ExampleEntry(
                    "Still life, table with fruits, vases, wine and olive leaves",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664454041.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664454041.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664454041.jpg"
                    )
            ),
            new ExampleEntry(
                    "A day at sea beach, woman figure in white dress and white hat",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737665007817.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737665007817.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737665007817.jpg"
                    )
            ),
            new ExampleEntry(
                    "Blue door and pink roses, stone masonry path, tidy old house entrance",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737665805083.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737665805083.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737665805083.jpg"
                    )
            )
    );
    public static final List<ExampleEntry> POST_IMPRESSIONISM_ART_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Wooden bench in a city park, people with umbrellas in rainy weather, rays of sun coming through clouds",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737663890368.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737663890368.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737663890368.jpg"
                    )
            ),
            new ExampleEntry(
                    "Still life, table with fruits, vases, wine and olive leaves",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737664485881.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737664485881.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737664485881.jpg"
                    )
            ),
            new ExampleEntry(
                    "A day at sea beach, woman figure in white dress and white hat",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737665032060.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737665032060.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737665032060.jpg"
                    )
            ),
            new ExampleEntry(
                    "Blue door and pink roses, stone masonry path, tidy old house entrance",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1737665864621.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1737665864621.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1737665864621.jpg"
                    )
            )
    );
    public static final List<ExampleEntry> NEBULA_EXAMPLES = Arrays.asList(
            new ExampleEntry(
                    "Pink theme, blonde, blue eyes",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1736879934311.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1736879934311.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1736879934311.jpg"
                    )
            ),
            new ExampleEntry(
                    "AI robot, mechanical parts",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1736879214730.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1736879214730.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1736879214730.jpg"
                    )
            ),
            new ExampleEntry(
                    "Neon lights, middle of the street, raining",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1736879319142.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1736879319142.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1736879319142.jpg"
                    )
            ),
            new ExampleEntry(
                    "Black hair, curly, sea view",
                    new ImageDTO(
                            "https://storage.googleapis.com/aiconnect/img_gen/54/full/Q1736879356017.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/comp/C1736879356017.jpg",
                            "https://storage.googleapis.com/aiconnect/img_gen/54/thumb/T1736879356017.jpg"
                    )
            )
    );
}
