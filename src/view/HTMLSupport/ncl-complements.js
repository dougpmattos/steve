//array com codigo e nome da tecla
var objectCodeNames =  {112:'F1' , 113:'F2' ,114:'F3' ,115:'F4' ,116:'F5' ,117:'F6' ,118:'F7' ,119:'F8' ,120:'F9' ,121:'F10' ,122:'F11' ,123:'F12' ,8:'BACK_SPACE' ,32:'SPACE' , 9:'TAB' ,16:'SHIFT' ,17:'CONTROL' ,18:'ALT' ,20:'CAPS_LOCK' ,33:'PAGE_UP' ,34:'PAGE_DOWN' ,35:'END' , 36:'HOME' ,19:'PAUSE_BREAK' ,42:'PRINTSCREEN' ,45:'INSERT' , 46:'DELETE' ,96:'NUMPAD0' ,97:'NUMPAD1' ,98:'NUMPAD2' ,99:'NUMPAD3' ,100:'NUMPAD4' ,101:'NUMPAD5' ,102:'NUMPAD6' ,103:'NUMPAD7' ,104:'NUMPAD8' ,105:'NUMPAD9' ,106:'NUMPAD*' ,107:'NUMPAD+' ,108:'NUMPAD,' ,109:'NUMPAD-' ,110:'NUMPAD.' ,144:'NUM_LOCK'}
var keyCodeMap = {
        8:"backspace", 9:"tab", 13:"return", 16:"shift", 17:"ctrl", 18:"alt", 19:"pausebreak", 20:"capslock", 27:"escape", 32:" ", 33:"pageup",
        34:"pagedown", 35:"end", 36:"home", 37:"left", 38:"up", 39:"right", 40:"down", 43:"+", 44:"printscreen", 45:"insert", 46:"delete",
        48:"0", 49:"1", 50:"2", 51:"3", 52:"4", 53:"5", 54:"6", 55:"7", 56:"8", 57:"9", 59:";",
        61:"=", 65:"a", 66:"b", 67:"c", 68:"d", 69:"e", 70:"f", 71:"g", 72:"h", 73:"i", 74:"j", 75:"k", 76:"l",
        77:"m", 78:"n", 79:"o", 80:"p", 81:"q", 82:"r", 83:"s", 84:"t", 85:"u", 86:"v", 87:"w", 88:"x", 89:"y", 90:"z",
        //96:"0", 97:"1", 98:"2", 99:"3", 100:"4", 101:"5", 102:"6", 103:"7", 104:"8", 105:"9",
        106: "*", 107:"+", 109:"-", 110:".", 111: "/",
        112:"f1", 113:"f2", 114:"f3", 115:"f4", 116:"f5", 117:"f6", 118:"f7", 119:"f8", 120:"f9", 121:"f10", 122:"f11", 123:"f12",
        144:"numlock", 145:"scrolllock", 186:";", 187:"=", 188:",", 189:"-", 190:".", 191:"/", 192:"`", 219:"[", 220:"\\", 221:"]", 222:"'"
}
//array de uso de teclas
var nclKeys = {};
var hasNclKeys = false;
var agenda = false;
var agendados = {};
var zIndex = 0;
var currentContext = 'body';
var winW, winH;


function keyPressedNCL(e){
	var test = nclKeys[e.keyCode];
	if(test)
		$('.started').trigger(test);
	test = null;
	/*for(var k in nclKeys){
		if(k.indexOf('Name')<0){
			if(nclKeys[k]==e.keyCode){
				$('.started').trigger(k);
				return;
			}
		}
	}*/
	key = parseInt(e.keyCode);
	var focus = -1;
	//var nomeContext = 'body'
	var ativeFocus = $(".active .started").parent('div[focusIndex]');	
	if(ativeFocus.length <1){
		$('.active').removeClass('.active');
		$('div[focusIndex] .started').parent('div[focusIndex]:first').addClass("active");
		if($('.active').length>0){
			return;
		}
	}
	else if(ativeFocus.length >1 ){
		ativeFocus.slice(1).removeClass("active");
	}
	switch(key){
		case 37:{
			focus = ativeFocus.first().attr("moveLeft");
			break;
		}
		case 38:{
			focus = ativeFocus.first().attr("moveUp");
			break;
		}
		case 39:{
			focus = ativeFocus.first().attr("moveRight");
			break;
		}
		case 40:{
			focus = ativeFocus.first().attr("moveDown");
			break;
		}
		case 13:{
			select(ativeFocus.first().children('.started').attr('id'));
			$('*').trigger('ENTER');
			break;
		}
	}
	/*
		console.log('entrou '+focus);
		focus = parseInt(focus);
		menor =focus;
		imediatamenteMaior = focus;
		$('div[focusIndex] .started[context="'+nomeContext+'"]').parent('div[focusIndex]').each(function(){
			emteste = parseInt($(this).attr('focusIndex'));
			if(emteste<menor)
				menor = emteste
			else if(imediatamenteMaior>=emteste>focus)
				imediatamenteMaior = emteste;
		})
		if(imediatamenteMaior == focus){
			focus = menor
		}
	*/
	if($('[focusIndex='+focus+'] .started').length>0){
		var atributos = $('span.settings input.property[name="service.currentFocus"]');
		if(atributos.length>0){
			atributos.each(function(){
				set($(this).parent('span').attr('id'),focus,"service.currentFocus");
			})
		}
		else{
			if(ativeFocus)
				ativeFocus.removeClass("active");
			var selecionado = $('div[focusIndex='+focus+']').find('.started').first().parent('div[focusIndex='+focus+']');
			selecionado.addClass('active');
		}
	}
}


function interpretaPropriedades(no){
	if(no.hasClass('settings'))
		return;
	var descriptor = no.parent('.descriptor').first();
	if(descriptor){
		descriptor.children('input.descriptorParam').each(function(){
			interpretaPropriedade(no,$(this));
		});
	}

	no.children('input.property').each(function(){
		interpretaPropriedade(no,$(this));
	});
	if(no.children('input.property').length == 0){
		$('input.property[parent="'+no.attr('id')+'"]').each(function(){
			interpretaPropriedade(no,$(this));
		});
	}
}

function interpretaPropriedade(no,Inter){
	if(Inter.attr('value')){
		if(Inter.attr('name')=='bounds'){
			var bounds = Inter.attr('value').split(',');//left top width height
			var leftV = $.trim(bounds[0]);
			var topV = $.trim(bounds[1]);
			var widthV = $.trim(bounds[2]);
			var heightV = $.trim(bounds[3]);
			var aux = topV.indexOf("%");
			if(aux>0)
				topV = (winH*parseFloat(topV.substring(0,aux)))/100.0;
			aux = leftV.indexOf("%");
			if(aux>0)
				leftV = (winW*parseFloat(leftV.substring(0,aux)))/100.0;
			aux = widthV.indexOf("%");
			if(widthV.indexOf("%")>0)
				widthV = (winW*parseFloat(widthV.substring(0,aux)))/100.0;
			aux = heightV.indexOf("%")
			if(aux>0)
				heightV = (winH*parseFloat(heightV.substring(0,aux)))/100.0;
			no.get(0).style.left = leftV+"px";
			no.get(0).style.top = topV+"px";
			no.get(0).style.width =widthV+"px";
			no.get(0).style.height = heightV+"px";
			if(!no.attr('resize')){
				$(window).resize(function() {
		  			interpretaPropriedade(no,Inter)
				});
				no.attr('resize','true');
			}
		}
		else if(Inter.attr('name')=='size'){
			var bounds = Inter.attr('value').split(',');//left top width height
			var widthV = $.trim(bounds[0]);
			var heightV = $.trim(bounds[1]);
			var aux = widthV.indexOf("%");
			if(aux.indexOf("%")>0)
				widthV = winW*(parseFloat(widthV.substring(0,aux))/100.0);
			aux = heightV.indexOf("%")
			if(aux>0)
				heightV = winH*(parseFloat(heightV.substring(0,aux))/100.0);
			no.get(0).style.width = widthV+"px";
			no.get(0).style.height = heightV+"px";
			if(!no.attr('resize')){
				$(window).resize(function() {
		  			interpretaPropriedade(no,Inter)
				});
				no.attr('resize','true');
			}
		}
		else if(Inter.attr('name')=='location'){
			var bounds = Inter.attr('value').split(',');//left top width height
			var leftV = $.trim(bounds[0]);
			var topV = $.trim(bounds[1]);
			var aux = topV.indexOf("%");
			if(aux>0)
				topV = winH*(parseFloat(topV.substring(0,aux))/100.0);
			aux = leftV.indexOf("%");
			if(aux>0)
				leftV = winW*(parseFloat(leftV.substring(0,aux))/100.0);
			no.get(0).style.left = leftV+"px";
			no.get(0).style.top = topV+"px";
			if(!no.attr('resize')){
				$(window).resize(function() {
		  			interpretaPropriedade(no,Inter)
				});
				no.attr('resize','true');
			}
		}
		else if(Inter.attr('name')=='soundLevel'){
			no.get(0).volume = parseFloat(Inter.attr('value'));
		}
		else if(Inter.attr('name')=='transparency'){
			var valor = Inter.attr('value');
			var aux = valor.indexOf("%");
			if(aux>0)
				no.get(0).style.opacity = 1.0- (parseFloat(valor.substring(0,aux))/100.0);
			else
				no.get(0).style.opacity = 1.0- parseFloat(valor);
		}
		else if(Inter.attr('name')=='visible'){
			if(Inter.attr('value')=='false'){
				no.get(0).style.visibility = 'hidden';
				no.get(0).style.display = "none";
			}
			else{
				no.get(0).style.visibility = 'visible';
				no.get(0).style.display = 'inline';
			}
		}
		else if(Inter.attr('name')=='scroll'){
			if(Inter.attr('value')=='false')
				no.get(0).style.overflow = 'hidden';
			 if(Inter.attr('value')=='automatic')
				no.get(0).style.overflow = 'auto';
			else
				no.get(0).style.overflow = 'scroll';
		}
		else if(Inter.attr('name')=='style'){
			no.addClass(Inter.attr('value'))
		}
		else if(Inter.attr('name')=='fontColor'){
			no.get(0).style.color = Inter.attr('value');
		}
		else{
			no.get(0).style[Inter.attr('name')] = Inter.attr('value');
		}
	}
}

function trataDescriptorInLink(id,descriptor,role){
	if($('#'+descriptor+'>#'+id).length>0){
		return;
	}
	if(role == 'stop'){
		$('#'+id+'_'+descriptor).remove();
	}
	else if(role == 'start'){
		if($('#'+id+"_"+descriptor).length==0){
			var original = $('#'+id);
			original.clone().attr('id',id+"_"+descriptor).attr('mirror',id).appendTo('#'+descriptor);
			var novo = $('#'+id+"_"+descriptor);
			original.bind('onEnd',function(){
				$('#'+id+'_'+descriptor).remove();
			});
			original.bind('onAbort',function(){
				$('#'+id+'_'+descriptor).remove();
			});
			novo.attr('state',null);
			novo.bind('onEnd',function(){
				$(this).remove();
			});
			novo.bind('onAbort',function(){
				$(this).remove();
			});
			start(id+"_"+descriptor);
		}
		else{
			start(id+"_"+descriptor);
		}
	}
}

function getSwitchNode(id,inter){
	if($('#'+id).attr('refer'))
		return getSwitchNode($('#'+id).attr('refer'),Inter);
	var retorno = null;
	if(inter){
		var components = $('#'+inter+' >li[rule]');
		components.each(function(index){
			if($(this).attr("rule")){
				if(window[$(this).attr("rule")]()){
					retorno = [$(this).attr("constituent"),$(this).attr("interface")];
				}
			}

		})
	}
	else{
		var components = $('#'+id+'>li[rule]');
		components.each(function(index){
			if($(this).attr("rule")){
				if(window[$(this).attr("rule")]()){
					retorno = [$(this).attr("constituent")];
				}
			}
		})
	}
	if(retorno)
		return retorno
	else if($('#'+id+' li[default]').length>0)
		return [$('#'+id).children('li[default]').attr("default")];
}

function trocaDescritor(idNo,idDescritor){
	var no = $("#"+idNo)
	if(no.parent('#'+idDescritor).length>0)
		return;
	var tempo =  0;
	if(no.get(0).currentTime){
		tempo = no.get(0).currentTime
		no.appendTo('#'+idDescritor);
		no.get(0).play();
		no.bind('loadedmetadata',function(){
			this.currentTime = tempo;
			no.unbind('loadedmetadata')
		});
	}
	else{
		no.appendTo('#'+idDescritor);
	}
	var regiaoPai = no.parents('.region');
	if(regiaoPai.first().attr('zIndex')){
		no.get(0).style.zIndex = parseInt(regiaoPai.first().attr('zIndex')) + regiaoPai.first().find('.started').length;
		// no.css('z-index',parseInt(regiaoPai.first().attr('zIndex')) + regiaoPai.first().find('.started').length);
	}
	else{
		no.get(0).style.zIndex = 'auto';
		//no.css('z-index','auto');
	}

}

function triggerEvento(id,Inter,evento,fromContext){
	//console.log(evento+":"+id+"."+Inter+"|"+fromContext);
	if(Inter){
		$('#'+Inter).trigger(evento,Inter);
		if(!fromContext){
			$('.port[value="'+id+'"][interface="'+Inter+'"]').each(function(){
				$(this).trigger(evento,this.id);
			});
			$('ul>li[component="'+id+'"][interface="'+Inter+'"]').each(function(){
				if(window[$(this).attr("rule")]()){
					otal = $(this).parent('ul');
					otal.trigger(evento,otal.attr('id'));
				}
			});
		}
	}
	else{
		$('#'+id).trigger(evento,id);
		trataInstSame(id,evento);
		if(!fromContext){
			$('.port[value="'+id+'"]').each(function(){
				$(this).trigger(evento,this.id);
			});
			$('ul>li[constituent="'+id+'"]').each(function(){
				otal = $(this).parent('ul');
				otal.trigger(evento,otal.attr('id'));

			});
		}
		$('ul>li[component="'+id+'"]').each(function(){
			if(!$(this).attr('interface')){
				otal = $(this).parent('ul');
				otal.trigger(evento,otal.attr('id'));
			}
		});
	}
}

//A ideia desta funÃ§Ã£o Ã© permitir ao nÃ³ vasio que Ã© inst same receber todos os eventos da instancia principal
function trataInstSame(idOriginal,evento){
	var estado = $('#'+idOriginal).attr('state');
	$('*[refer="'+idOriginal+'"][instance="instSame"],*[refer="'+idOriginal+'"][instance="gardSame"]').each(function(){
		if(estado)
			$(this).attr('state',estado);
		triggerEvento(this.id,null,evento);
	});
}



function resume(id,inter,fromContext){
	if(('#'+id+'[state="paused"]').length == 0)
		return
	var target = $('#'+id);
	if(target.attr('refer')&&target.attr('instance')=='instSame'){
		target.attr('state','paused');
		resume(target.attr('refer'));
		return;
	}
	target = null;
	var media = document.querySelector('#'+id);
	if(media.play){
		media.play();
	}
	else if($("div#"+id).length==1){
		if(inter){
			var porta = $('#'+inter);
			resume(porta.attr('value'),porta.attr('interface'),true);
		}
		else{
			$('#'+id+'>input.port').each(function(){
				resume($(this).attr('value'),$(this).attr('interface'),true);
			});
		}
	}
	else if($('ul#'+id).length==1){
		var swit = id
		if($('#'+id).attr('refer'))
			swit = $('#'+id).attr('refer');
		$("ul#"+swit+'>li').each(function(){
			pause($(this).attr('constituent'),$(this).attr('interface'),true);
		})
	}
	var media = $('#'+id);
	media.attr('state','occurring');
	triggerEvento(id,inter,'onResume',fromContext);
	$('*[mirror="'+id+'"][state="occurring"]').each(function(){//start mirrors
		if(this.play){
			this.play();
		}
	});
}

function pause(id,inter,fromContext){
	if(('#'+id+'[state="occurring"]').length == 0)
		return
	var target = $('#'+id);
	if(target.attr('refer')&&target.attr('instance')=='instSame'){
		target.attr('state','occurring');
		pause(target.attr('refer'));
		return;
	}
	target = null;
	var media = document.querySelector('#'+id);
	if(media.pause){
		media.pause();
	}
	else if($("div#"+id).length==1){
		if(inter){
			var porta = $('#'+inter);
			pause(porta.attr('value'),porta.attr('interface'),true);
		}
		else{
			$('#'+id+'>input.port').each(function(){
				pause($(this).attr('value'),$(this).attr('interface'),true);
			});
		}
	}
	else if($('ul#'+id).length==1){
		var swit = id
		if($('#'+id).attr('refer'))
			swit = $('#'+id).attr('refer');
		$("ul#"+swit+'>li').each(function(){
			pause($(this).attr('constituent'),$(this).attr('interface'),true);
		})
	}
	media = $('#'+id);
	media.attr('state','paused');
	triggerEvento(id,inter,'onPause',fromContext);
	$('*[mirror="'+id+'"][state="occurring"]').each(function(){//start mirrors
		if(this.pause){
			this.pause();
		}
	});
}

function set(id,value,Inter,fromContext){
	//console.log('set '+id+"."+Inter+":"+value)
	var target = $('#'+id);
	if(target.attr('refer')&&target.attr('instance')=='instSame'){
		id = target.attr('refer');
		target = $('#'+id);
	}
	if(Inter){
		triggerEvento(id,null,'onBeginAttribution',fromContext);
		triggerEvento(id,Inter,'onBeginAttribution',fromContext);
		var porepertyMedia = target.children('input.property[name="'+Inter+'"]');
		if(porepertyMedia.length ==1){// Ã© uma midia
			porepertyMedia.attr('value',value);
			if(!target.hasClass('settings'))
				interpretaPropriedade(target,porepertyMedia);
			else{
				if(Inter == "service.currentFocus"){
					$('.active').removeClass("active");
					$('.descriptor[focusIndex="'+value+'"]').addClass('active');
				}
			}
		}
		else if($('input#'+Inter+'.port').length ==1){//Ã© um contexto com uma porta
			var porta = $('input#'+Inter+'.port');
			set(porta.attr('value'),porta.attr('interface'),true);
		}
		else if($('ul#'+id).length == 1){//trata switch
			var noDeExec = getSwitchNode(id,Inter);
			if(noDeExec)
				set(noDeExec[0],value,noDeExec[1],true);
		}
		triggerEvento(id,Inter,'onEndAttribution',fromContext);
		triggerEvento(id,null,'onEndAttribution',fromContext);
	}
	else{//Ã© uma propriedade
		if(target.hasClass('property')){
			triggerEvento(id,null,'onBeginAttribution',fromContext);
			target.attr('value',value);
			triggerEvento(id,null,'onEndAttribution',fromContext);
		}
	}
	/*
	//trata gradSame
	$('*[refer="'+id+'"][instance="gardSame"]').each(function(){
		set(this.attr('id'),value,Inter);
	})*/
}

function select(id,Inter,fromContext){
	var selecionado = $('#'+id);
	if(selecionado.attr('refer')&&selecionado.attr('instance')=='instSame'){
		selecionado = $('#'+target.attr('refer'));
		id = target.attr('refer');
	}
	if(selecionado.hasClass('started')){
		if(selecionado.parent('[focusIndex]').length >0){
			$('.active').removeClass('active');
			selecionado.parent('[focusIndex]').addClass('active');
			var focus = selecionado.parent('[focusIndex]').attr('focusIndex');
			var atributos = $('span.settings input.property[name="service.currentFocus"]');
			if(atributos.length>0)
			atributos.each(function(){
				set($(this).parent('span').attr('id'),focus,"service.currentFocus");
			})
			if($("div#"+id).length==1){
				if(Inter){
					triggerEvento(id,Inter,"onSelection",fromContext);
					var porta = $('#'+Inter);
					select(porta.attr('value'),porta.attr('interface'),true);
				}
				else{
					$(currentContext+'> .port').each(function(index){
						select(this.value,$(this).attr('interface'),true);
					})
				}
			}
			if($('ul#'+id).length == 1){//trata switch
				var noDeExec = getSwitchNode(id,Inter);
				if(noDeExec)
					select(noDeExec[0],noDeExec[1],true);
			}
		}
		triggerEvento(id,null,"onSelection",fromContext);

	}
}

function abort(id,Inter,fromContext){
	if($('#'+id+'[state="sleeping"]').length == 1){
		$('*[mirror="'+id+'"]').each(function(){
			if(this.id.indexOf('_')>=0)
				$(this).remove();
		});
		return;
	}
	var target = $('#'+id);
	if(target.attr('refer')&&target.attr('instance')=='instSame'){
		target.attr('state','sleeping');
		abort(target.attr('refer'));
		return;
	}
	target = null;
	if($("div#"+id).length==1){
		if(Inter){
			var porta = $('#'+Inter);
			abort(porta.attr('value'),porta.attr('interface'),true);
		}
		else{
			$('.started[context="'+id+'"]').each(function(){
				abort(this.id,null,true);
			});
			var currentContext = $("div#"+id).attr('context');
			if(currentContext != 'body')
				currentContext = '#'+currentContext;

		}
	}
	else if($("ul#"+id).length ==1){
		var swit = id
		if($('#'+id).attr('refer'))
			swit = $('#'+id).attr('refer');
		$("ul#"+swit+'>li').each(function(){
			abort($(this).attr('constituent'),$(this).attr('interface'),true);
		})
	}
	stopImage($('#'+id),id,Inter,fromContext,"onAbort");

}

function stop(id,Inter,fromContext){
	console.log('stop '+id);
	var target = $('#'+id);
	if(target.attr('state')=="sleeping"){
		$('*[mirror="'+id+'"]').each(function(){
			if(this.id.indexOf('_')>=0)
				$(this).remove();
		});
		return;
	}

	if(target.attr('refer')&&target.attr('instance')=='instSame'){
		target.attr('state','sleeping');
		stop(target.attr('refer'));
		return;
	}
	var target = null;
	if($("div#"+id).length==1){
		if(Inter){
			var porta = $('#'+Inter);
			stop(porta.attr('value'),porta.attr('interface'),true);
		}
		else{
			$('*[state="occurring"][context="'+id+'"]').each(function(){
				stop(this.id,null,true);
			});
			var currentContext = $("div#"+id).attr('context');
			if(currentContext != 'body')
				currentContext = '#'+currentContext;

		}
	}
	else if($("ul#"+id).length ==1){
		var swit = id
		if($('#'+id).attr('refer'))
			swit = $('#'+id).attr('refer');
		$("ul#"+swit+'>li[constituent]').each(function(){
			stop($(this).attr('constituent'),$(this).attr('interface'),true);
		})
	}
	var media =  $('#'+id);
	var dPai = media.parent('.descriptor[transOut]').first();
	if(dPai.length>0){//a implementar outras transiÃ§Ãµes
		var t = dPai.attr('transOut');
		//if(transicoes[t].type=='fade'){
			var it = Inter;
			var con = fromContext;
			var locId = id;
			media.fadeOut(parseFloat(transicoes[t].dur.substring(0,transicoes[t].dur.length-1)*1000),function(){
				$(this).removeAttr('style');
				console.log('teste:'+locId)
				stopImage($(this),locId,it,con,"onEnd");
			})
		//}
	}
	else{
		stopImage(media,id,Inter,fromContext,"onEnd");
	}
}
function stopImage(media,id,Inter,fromContext,event){
	console.log('parou'+id);
	media.removeClass("started");
	media.addClass("stoped");
	media.attr('state','sleeping');
	triggerEvento(id,Inter,event,fromContext);
	$('*[mirror="'+id+'"]').each(function(){
		stop(this.id,null,true);
	});
	if(media.parent('.active').length >0 && media.parent('.active').children('*[state="occurring"]').length ==0){//remove seleÃ§Ã£o
		media.parent('.active').removeClass('active');
	}
	apagaAncoras(id,event=='onEnd');
	var me = media.get(0);
	if(me.pause){
		me.pause();
		if(me.currentTime)
			me.currentTime = 0;
		me.preload ="none";
	}
	if($('*[state="occurring"]*[context="'+media.attr('context')+'"]').length == 0 && $('#'+media.attr('context')).attr('state')=='occurring'&&!fromContext){
		//console.log('para contexto pai:'+media.attr('context'));
		if(media.attr('context') && media.attr('context') != 'body'){
			$('#'+media.attr('context')).attr('state','sleeping');
			triggerEvento(media.attr('context'),Inter,event,fromContext);
		}
		else{
			if($('.started').length==0)
				$('body').trigger(event,id);
		}
	}
	if(!fromContext){
		$('li[constituent="'+id+'"]').parent('ul[state="occurring"]').attr('state','sleeping');
	}
}

function start(id,Inter,fromContext){
	//console.log('start '+id+":"+Inter)
	/*if($('#'+id+'[state="occurring"]').length == 1){
		return
	}*/
	target = $('#'+id);
	if(target.attr('refer')&&target.attr('instance')=='instSame'){
		target.attr('state','occurring');
		start(target.attr('refer'));
		return;
	}
	targer = null;
	var media = document.getElementById(id);
	if(media.play){
		media.preload ="auto";
		if(media.currentTime)
			media.currentTime = 0;
		media.play();
		if(Inter){
			inicio =  $('area#'+Inter).attr('begin');
			$('#'+id).bind('playing',function(){
				media = document.getElementById(id);
				if(media.seekable.end(0)>= parseFloat(inicio.replace('s',''))){
					media.currentTime = inicio.replace('s','');
					$('area#'+Inter).attr('started','started');
					$('#'+id).unbind('playing');
					triggerEvento(id,Inter,"onBegin",fromContext);
				}
			});
		}
	}
	media = $('#'+id);
	if($('ul#'+id).length == 1){//trata switch
		noDeExec = getSwitchNode(id,Inter);
		if(Inter)
			triggerEvento(id,Inter,"onBegin",fromContext);
		if(noDeExec)
			start(noDeExec[0],noDeExec[1],true);
		else
			return;
	}
	if($("div#"+id).length==1){//trata contexto
		if($('#'+id).attr('refer')){
			start($('#'+id).attr('refer'),Inter,true);
		}
		else
			startContext('#'+id,Inter);
	}
	else if(media.parents('.region').length>0){
		regiaoPai = media.parents('.region');
		if(!regiaoPai.first().attr('zIndex')|| regiaoPai.length ==0){
			media.get(0).style.zIndex = zIndex;
			//media.css('z-index',zIndex);
			zIndex++;
		}
		else{
			indice = parseInt(regiaoPai.first().attr('zIndex')) + regiaoPai.first().find('.started').length;
			regiaoPai.first().find('.started').each(function(){
					if(this.style.zIndex>=indice)
						this.style.zIndex= indice-1;
			});
			media.get(0).style.zIndex = indice;
			//media.css('z-index',indice);
		}
	}
	if(media.parent('.descriptor[transIn]:first').length>0){
		t = media.parent('.descriptor[transIn]:first').attr('transIn');
		var lmedia = media;
		var lid = id;
		var lfC = fromContext
		//if(transicoes[t].type=='fade'){
			media.fadeOut(0,function(){
				media.fadeIn(parseFloat(transicoes[t].dur.substring(0,transicoes[t].dur.length-1))*1000,function(){
					//if(lmedia.parent('.descriptor').length>0){
						lmedia.removeClass("stoped");
						lmedia.addClass("started");
					//}
					interpretaPropriedades(lmedia);
					lmedia.attr('state','occurring');
					triggerEvento(lid,null,"onBegin",lfC);
					if($('.active').length ==0 &&lmedia.parent().attr('focusIndex')!=null){//da seleÃ§Ã£o se tem focusIndex e nÃ£o hÃ¡ selecionado
						atributos = $('span.settings input.property[name="service.currentFocus"]');
						if(atributos.length>0){
							atributos.each(function(){
								set($(this).parent('span').attr('id'),lmedia.parent().attr('focusIndex'),"service.currentFocus");
							})
						}
						/*else{
							lmedia.parent().addClass('active');
						}*/
					}
				});
			});
		//}
	}
	else{
//		if(media.parent('.descriptor').length>0){
			media.removeClass("stoped");
			media.addClass("started");
//		}
		media.attr('state','occurring');
		triggerEvento(id,null,"onBegin",fromContext);
		interpretaPropriedades(media);
		if($('.active').length ==0 &&media.parent().attr('focusIndex')!=null){//da seleÃ§Ã£o se tem focusIndex e nÃ£o hÃ¡ selecionado
			atributos = $('span.settings input.property[name="service.currentFocus"]');
			if(atributos.length>0){
				atributos.each(function(){
					set($(this).parent('span').attr('id'),media.parent().attr('focusIndex'),"service.currentFocus");
				})
			}
			/*else{
				media.parent().addClass('active');
			}*/
		}
	}
	$('*[mirror="'+id+'"][state="occurring"]').each(function(){//start mirrors
		start(this.id,null,true);
	});
	var needCheck = false;
	if(media.parent('div[explicitDur]').length>0){//Mudar
		media.attr('dur',media.parent('div').attr('explicitDur').replace('s',''));
		agendados[id] = media;
		needCheck = true;
	}
	if(media.children('input.property[explicitDur]').length>0){//Mudar?
		media.attr('dur',media.children('input.property[explicitDur]').attr('explicitDur').replace('s',''));
		agendados[id] = media;
		needCheck = true;
	}
	if(media.children('area').length >0){
		agendados[id] = media;
		needCheck = true;
		media.children('area').each(function() {
			media.attr(this.id,'0');
		});
		console.log('dur na area');
	}
	else{
		var teste = false
		$('area[parent="'+id+'"]').each(function() {
			media.attr(this.id,'0');
			teste = true;
		});
		if(teste){
			agendados[id] = media;
			needCheck = true;
		}
	}
	if(needCheck&&!agenda){
		console.log(needCheck+" id:"+id);
		setTimeout("checkInterAnchors()",500);
		agenda = true;
	}
}

function apagaAncoras(id,ended){//ativado quando um nÃ³ Ã© parado
	for(var k in agendados){
		if(agendados[k].attr('id') == id){
			if(ended){
//				console.log('ended');
				agendados[k].children('area[begin]:not([end])').each(function(){
					triggerEvento(agendados[k].attr('id'),this.id,"onEnd");
				});
			}
			delete agendados[k];
		}
	}
}

//funÃ§Ã£o que Ã© executada de meio em meio segundo em caso de ancoras
function checkInterAnchors(){
	var entra = false;
//	console.log('testa ancoras');
	for(var k in agendados){
		if(agendados[k].attr('state') == 'occurring'){
			if(agendados[k].attr('dur')){
				var dur = parseFloat(agendados[k].attr('dur'));
				dur = dur -0.5;
				if(dur <=0)
					stop(k);
				else
					agendados[k].attr('dur',dur);
				entra =true
			}
			if(agendados[k]){
				var time = 0
				var teste = false
				if(agendados[k].get(0).currentTime){
					time = parseFloat(agendados[k].get(0).currentTime);
				}
				else
					time = parseFloat(agendados[k].attr(k))+0.5;
				$('area[parent="'+k+'"]').each(function() {
					teste = true
					id = this.id;
					inicio = false
					if(agendados[k].attr(id)){
						if(time>=parseFloat($(this).attr('begin')) && !$(this).attr('started')){
							$(this).attr('started','started');
							triggerEvento(k,id,"onBegin");
						}
						else if($(this).attr('end')){
							if(time>=parseFloat($(this).attr('end')) && $(this).attr('started')){
								triggerEvento(k,id,"onEnd");
								$(this).removeAttr('started');
								agendados[k].removeAttr(id);
							}
							else
								agendados[k].attr(id,time);
						}
						else if($(this).attr('started')){
							agendados[k].removeAttr(id);
						}
						else{
							agendados[k].attr(id,time);
						}
					}
				});
				if(teste){
					entra =true
				}
			}
		}
	}
	if(entra)
		setTimeout("checkInterAnchors()",500);
	else{
		agenda = false;
	}
}

function startContext(id,Inter){
	currentContext = id;
	if(Inter){
		porta = $('#'+Inter);
		start(porta.attr('value'),porta.attr('interface'),true);
	}
	else{
		$(currentContext+' > .port').each(function(index){
			start(this.value,$(this).attr('interface'),true);
		})
	}
}

function iniciaDocumentoNCL(){
	$(document).keydown(keyPressedNCL);
	//linha de codigo para pegar o tamanho da janela
	$(window).resize(function() {
		  carregaTela();
	});
	carregaTela();
	//fim da linha de codigo
	$('img,vide,iframe').each(function(){$(this).addClass("stoped")});
	$('video,audio').each(function(index){
			$(this).addClass("stoped");
			var id = this.id;
			$(this).bind('ended',{id:this.id},function(evt){
				//afazertestar se o descritor tem frezze
				stop(this.id);
			});
			$(this).bind('abort',{id:this.id},function(evt){
				abort(this.id);
			});
	});
	//$(document).trigger('inicio');
	startContext('body');
}
/*$(document).ready(function(){
	iniciaDocumentoNCL();
});*/

$(document).ready(function(){
	for(var k in nclKeys)
	{
		key = readCookie(k);
		if(key){
			nclKeys[k] = parseInt(key);
			if(isNaN(nclKeys[k]))
				nclKeys[k] = key;
		}
	} 
	
	//percorrer ncl keys mapeando cada uma pra um valor hardcoded
	keyNumber = 0;

	// Chamada assincrona le o arquivo JSON com as teclas definidas na preferences do Steve
    var objJson = $.ajax({
      type: "GET",
      url: "saida.json",
      async: false,
      dataType: "json"
 	});

    objKeys = JSON.parse(objJson.responseText); // objKeys[key]
    
	var b = 0;
	var g = 0;
	var r = 0;
	var y = 0;

	// Recupera o codigo da tecla em javascript mapeada no Steve
	for (var key in objKeys) {

		if (objKeys.hasOwnProperty(key)) {
		  	if(key=="blue"){
		  		for(v in keyCodeMap){
		  			if(keyCodeMap[v]==objKeys["blue"]){
		  				console.log("Blue key is: "+keyCodeMap[v]+" and code is: "+v);
		  				b = v;
		  			}
		  		}		  		
		  	}
		  	if(key=='red'){
		  		for(v in keyCodeMap){
		  			if(keyCodeMap[v]==objKeys["red"]){
		  				console.log("Red key is: "+keyCodeMap[v]+" and code is: "+v);
		  				r = v;
		  			}
		  		}		  	
		  	}
		  	if(key=='green'){
		  		for(v in keyCodeMap){
		  			if(keyCodeMap[v]==objKeys["green"]){
		  				console.log("Green key is: "+keyCodeMap[v]+" and code is: "+v);
		  				g = v;
		  			}
		  		}		  		
		  	}
		  	if(key=='yellow'){
		  		for(v in keyCodeMap){
		  			if(keyCodeMap[v]==objKeys["yellow"]){
		  				console.log("Yellow key is: "+keyCodeMap[v]+" and code is: "+v);
		  				y = v;
		  			}
		  		}		  	
		  	}
		    console.log(key + " -> " + objKeys[key]);		    
		}
	}
	
	// Atribui os valores do codigo de tecla javascript s
	for (var k in nclKeys) {

		console.log("Var k = "+k);
		if(k.indexOf('Name')<0){
			if(k=='Blue'){
				nclKeys[k]=b;
				keyNumber = k;
			}
			if(k=='Red'){
				nclKeys[k]=r;
				keyNumber = k;
			}		
			if(k=='Yellow'){
				nclKeys[k]=y;
				keyNumber = k;
			}		
			if(k=='Green'){
				nclKeys[k]=g;
				keyNumber = k;
			}			

		}		
		else {
			var nome = objectCodeNames[k.keyCode];
			console.log("k.keyCode = "+k.keyCode);
			if(!nome)
			{
				nome = keyNumber-49;
			}
			if(!nome||nome==''){
				nome = String.fromCharCode((96 <= keyNumber && keyNumber <= 105)? keyNumber-49 : keyNumber)
			}
			nclKeys[k] = nome;
		}
	}
	descarregaVariaveis();
	iniciaDocumentoNCL();

	//inciaPaginaConfig();
	//no final o java que vai passar pra ac
});


//Abrir arquivo
function readTextFile(file)
{
    var rawFile = new XMLHttpRequest();
    rawFile.open("GET", file, false);
    rawFile.onreadystatechange = function ()
    {
        if(rawFile.readyState === 4)
        {
            if(rawFile.status === 200 || rawFile.status == 0)
            {
                var allText = rawFile.responseText;
                console.log(allText);
            }
        }
    }
    rawFile.send(null);
}

//Janela inicial
function keyPressedPre(e){
	if($('.selecionadoPre').length ==1){
		$('#'+id).fadeOut(0);
		var id = $('.selecionadoPre').attr('id');
		var nome = objectCodeNames[e.keyCode];
		if(!nome)
		{
			nome = e.originalEvent.keyIdentifier;
		}
		if(!nome||nome==''){
			nome = String.fromCharCode((96 <= key && key <= 105)? key-48 : key)
		}
		for(var k in nclKeys){
			if(k.indexOf('Name')<0){
				if(nclKeys[k]==parseInt(e.keyCode)){
					$('#'+id).fadeIn('slow');
					return;
				}
			}
		}
		$('#'+id).removeClass("selecionadoPre");
		console.log("E = "+ e.keyCode);
		//nclKeys[id] = parseInt(84);
		nclKeys[id] = parseInt(e.keyCode);
		nclKeys[id+'Name'] = nome;
		$('#'+id).html(nome);
		createCookie(id,e.keyCode,7);
		createCookie(id+'Name',nome,7);

		$('#'+id).fadeIn('slow');
	}
	else{
		$('.selecionadoPre').slice(1).html("clique aqui para escolher");
		$('.selecionadoPre').slice(1).removeClass("selecionadoPre");
	}
}

function inciaPaginaConfig(){
	estilo = "width:30em;    height:85%;   position:absolute;    left:10%;    top:5%;    background:#FFF;    z-index:9900;    padding:10px;    border-radius:10px;";
	janela = '<div style="'+estilo+'" id="janela1">';
	janela += '<span style="text-align:left;">Trazido a vocÃª por Esdras Caleb (caleb@midiacom.uff.br)</span><a href="#" id="fechar" style=" padding-left:2em; text-align:right;">X Fechar</a>';
	janela += '<h4>Escolha as teclas que irÃ£o ativar os seguintes botÃµes do controle remoto (para selecionar clique na tecla, nem todas as teclas sÃ£o customizÃ¡veis):</h4>';
	estilo = 'margin-top: 1em;font-size:0.8em; border-collapse: collapse; margin-left:7%;';
	janela += '<table style="'+estilo+'" >';
	estilo = "padding: .3em; border: 1px #ccc solid; background:C3C3C3; ";
	janela += '<thead style="background: #E9E9E9;" >';
	janela += '<tr><td style="'+estilo+'">Tecla no Controle Remoto</td><td style="'+estilo+'">Tecla Mapeada</td></tr>';
	janela += '</thead>';
	janela += '<tbody>';
	janela += '<tr><td style="'+estilo+'">&larr;</td><b><td style="'+estilo+'">&#9668;</td></b></tr>';
	janela += '<tr><td style="'+estilo+'">&rarr;</td><td style="'+estilo+'">&#9658;</td></tr>';
	janela += '<tr><td style="'+estilo+'">&darr;</td><td style="'+estilo+'">&#9660;</td></tr>';
	janela += '<tr><td style="'+estilo+'">&uarr;</td><td style="'+estilo+'">&#9650;</td></tr>';
	janela += '<tr><td style="'+estilo+'">ENTER</td><td style="'+estilo+'">ENTER</td></tr>';
	estilo = "padding: .3em; border: 1px #ccc solid; ";
	for(var k in nclKeys)
		if(k.indexOf("Name")<0){
			janela += '<tr><td style="'+estilo+'">'+k+'</td><td style="'+estilo+'"><a id="'+k+'" href="#">'+nclKeys[k+'Name']+'</a></td></tr>';
		}
	janela += '</tbody>';
	janela += '</table>';
	janela += '<p>O sistema salva automaticamente suas escolhas. <br/><input type="checkbox" name="chNoMore" id="chNomore" value="noMore"> Marque aqui para nÃ£o exibir mais este aviso</p>';
	janela += '</div>';
	estilo = "width:100%;    height:100%; position:absolute;    left:0;    top:0;    z-index:9000;    background-color:gray;";
	janela +='<div style="'+estilo+'" id="mascara"></div>';
	$('body').append(janela);
	$('td a').click(function (e){
		if($('.selecionadoPre').length ==0){
			id = this.id;
			nclKeys[id] = null;
			$('#'+id).fadeOut(0);
			$(this).html('Digite a tecla');
			$('#'+id).addClass('selecionadoPre');
			$('#'+id).fadeIn('slow');
		}
	})
	$('#chNomore').change(function(){
		if($(this).attr('checked'))
			createCookie('pula','true',7);
		else
			createCookie('pula','false',7);
	})
	$(document).keydown(keyPressedPre);
	$("#fechar").click( function(){
		$(document).unbind('keydown',keyPressedPre);
		$('#janela1').remove();
		$('#mascara').remove();
		descarregaVariaveis();
		iniciaDocumentoNCL();
	});
}

function descarregaVariaveis(){
	objectCodeNames = null;
	var newKeys = {}
	for(var k in nclKeys){
		console.log('A chave '+nclKeys[k]+' foi definida');		
		console.log('K = '+k+' foi definida');
		//console.log('A chaveName '+nclKeys[k+Name]+' foi definida');

		if(k.indexOf('Name')<0){
			newKeys[nclKeys[k]] = k;
			console.log('nclKeys[k] = '+nclKeys[k]);
			console.log('newKeys[nclKeys[k]] = '+newKeys[nclKeys[k]]);

		}
		nclKeys[k] = null;
	}
	nclKeys = newKeys;
}

//funÃ§Ãµes auxiliares
function carregaTela(){
	if (document.body && document.body.offsetWidth) {
	 winW = document.body.offsetWidth;
	 winH = document.body.offsetHeight;
	}
	if (document.compatMode=='CSS1Compat' &&
	    document.documentElement &&
	    document.documentElement.offsetWidth ) {
	 winW = document.documentElement.offsetWidth;
	 winH = document.documentElement.offsetHeight;
	}
	if (window.innerWidth && window.innerHeight) {
	 winW = window.innerWidth;
	 winH = window.innerHeight;
	}
}
function createCookie(name,value,days){
	if (days) {
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+encodeURIComponent(value)+expires;
}

function readCookie(name){
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return decodeURIComponent(c.substring(nameEQ.length,c.length));
	}
	return null;
}