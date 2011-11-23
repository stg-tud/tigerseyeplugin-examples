package de.tud.stg.tigerseye.examples.logo
//In order to work the TranslationTransformer transformer has to be activated:
//Preferences->Tigerseye->Transformations: choose dsl for this extension,
//choose Edit DSL specific Transformers and activate the TranslationTransformer 

//The path to the actual translation file has to be provided either as project relative path beginning with a "/"
// or as relative path to the folder of the defining file. 
@Translation(file="translation.jpn")
logo(name:'japlogo'){
	オンワード 50
	みぎ 90
	あと 50

	ひだり 90
	オンワード 50
	みぎ 90
}