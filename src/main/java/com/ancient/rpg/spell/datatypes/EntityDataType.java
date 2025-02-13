package com.ancient.rpg.spell.datatypes;

import java.util.UUID;

import org.bukkit.entity.Entity;

import com.ancient.rpg.parameter.Parameter;
import com.ancient.rpg.parameter.ParameterType;
import com.ancient.rpg.spell.DataType;
import com.ancient.rpg.spell.SpellItem;
import com.ancient.rpg.spell.SpellParser;
import com.ancient.rpg.spellmaker.Returnable;
import com.ancient.util.EntityFinder;

public class EntityDataType extends DataType<Entity> {
	private Entity value;
	private Returnable<Entity> valueItem;
	
	@SuppressWarnings("unchecked")
	public EntityDataType(int line, String value) {
		super(line, "<html>An entity data type, which can store an <b>entity</b>.</html>");
		
		try {
			this.value = EntityFinder.findByUUID(UUID.fromString(value));
			
			if (this.value == null) {} // gibt keine entity mit der uuid...
		} catch (NumberFormatException ex) {
			SpellItem item = SpellParser.parse(value, line);
			if (item instanceof Returnable) this.valueItem = (Returnable<Entity>) item;
			else {} // exception. kann nicht verwendet werden.
		}
	}

	@Override
	public Entity getValue() {
		if (this.valueItem != null) calculateReturn();
		
		return this.value;
	}

	private void calculateReturn() {
		this.value = this.valueItem.getValue();
	}

	@Override
	public Parameter getReturnType() {
		return new Parameter(ParameterType.ENTITY, false);
	}
}
