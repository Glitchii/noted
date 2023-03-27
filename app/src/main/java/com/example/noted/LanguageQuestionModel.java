package com.example.noted;

public class LanguageQuestionModel {
    protected String[] options;
    protected String englishText;
    protected String translatedText;

    public LanguageQuestionModel(String englishText, String translatedText, String[] options) {
        this.options = options;
        this.englishText = englishText;
        this.translatedText = translatedText;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String getEnglishText() {
        return englishText;
    }

    public void setEnglishText(String englishText) {
        this.englishText = englishText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }
}
