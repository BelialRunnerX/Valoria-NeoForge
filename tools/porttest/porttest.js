// PORT NOTE (port metadata - dev tooling): in-game smoke test for the NeoForge 1.21.1 port.
// KubeJS server script. Copy to <run dir>/kubejs/server_scripts/porttest.js, start the client with
// `gradlew runClientAuto` (quick-plays the PortTest world) and read logs/kubejs/server.log for "[PORTTEST]" lines.
// Every check logs PASS or FAIL; the run ends with "[PORTTEST] DONE <pass>/<total>".
// The script only runs once per world load and only while at least one player is online.

const TAG = '[PORTTEST]'
const AX = 2000, AY = 150, AZ = 2000 // arena centre (overworld), platform at AY-1

let steps = []
let started = false
let t0 = 0
let pass = 0, total = 0
let before = {}

// FOCUS = true runs only the curio / nihility / natural-crypt sections (quick iteration); false = everything
const FOCUS = false
const SKIP_IN_FOCUS = /^(kiln|dispenser|disc|enchant|painting|portal|mob |boss |structure )/

function log(ok, name, detail) {
  total++
  if (ok) pass++
  console.log(TAG + ' ' + (ok ? 'PASS' : 'FAIL') + ' ' + name + (detail ? ' - ' + detail : ''))
}
function info(msg) { console.log(TAG + ' INFO ' + msg) }
function at(tick, name, fn) { if (FOCUS && SKIP_IN_FOCUS.test(name)) return; steps.push({ tick: tick, name: name, fn: fn }) }
function ensureAlive(s, p) {
  // singleplayer keeps the host player inside level.dat, so a run that ends with a dead player reloads dead (death screen).
  // PlayerList.respawn() did not take here; reviving in place does.
  if (p.health <= 0 || p.isDeadOrDying()) {
    try {
      info('test player dead at start (removed=' + p.isRemoved() + ' deathTime=' + p.deathTime + ' players=' + s.players.size() + '); reviving in place')
      p.deathTime = 0
      p.setHealth(p.getMaxHealth())
      p.setInvulnerable(false)
      info('after revive: alive=' + p.isAlive() + ' hp=' + p.health)
    } catch (e) { info('revive failed: ' + e) }
    return false
  }
  return true
}
function cmd(server, c) { server.runCommandSilent(c) }
function block(server, dim, x, y, z) { return server.getLevel(dim).getBlock(x, y, z) }
function entitiesOfType(server, type) {
  let n = 0
  server.entities.forEach(e => { if (String(e.type) == type) n++ })
  return n
}
function itemDrops(server) {
  let ids = []
  server.entities.forEach(e => { if (String(e.type) == 'minecraft:item') ids.push(String(e.item.id)) })
  return ids
}
function dimOf(player) { return String(player.level.dimension) }
function attachments(player) {
  try {
    let a = player.nbt.getCompound('neoforge:attachments')
    let out = []
    a.getAllKeys().forEach(k => { if (String(k).indexOf('valoria:') == 0) out.push(k + '=' + String(a.get(k))) })
    return out.join(' | ')
  } catch (e) { return 'ERR ' + e }
}

// ---------------------------------------------------------------- setup
at(3, 'setup respawn', (s, p) => { ensureAlive(s, p) })
at(5, 'setup', (s, p) => {
  cmd(s, 'attribute @a minecraft:generic.max_health base set 20')
  cmd(s, 'gamerule doDaylightCycle false')
  cmd(s, 'gamerule doWeatherCycle false')
  cmd(s, 'gamerule doMobSpawning false')
  cmd(s, 'time set day')
  cmd(s, 'difficulty normal')
  cmd(s, 'gamemode creative @a')
  cmd(s, 'execute in minecraft:overworld run forceload add 1970 1970 2030 2030')
  cmd(s, 'execute in minecraft:overworld run fill 1975 ' + (AY - 1) + ' 1975 2025 ' + (AY - 1) + ' 2025 minecraft:stone')
  cmd(s, 'execute in minecraft:overworld run fill 1975 ' + AY + ' 1975 2025 ' + (AY + 25) + ' 2025 minecraft:air')
  cmd(s, 'execute in minecraft:overworld run tp @a ' + AX + ' ' + AY + ' ' + AZ + ' 0 0')
  cmd(s, 'effect give @a minecraft:resistance 99999 4 true')
  cmd(s, 'effect give @a minecraft:regeneration 99999 4 true')
  cmd(s, 'effect give @a minecraft:night_vision 99999 0 true')
  cmd(s, 'clear @a')
  before.attachments = attachments(p)
  info('player ' + p.name.string + ' attachments before: ' + before.attachments)
})
at(15, 'arena ready', (s, p) => {
  log(String(block(s, 'minecraft:overworld', AX, AY - 1, AZ).id) == 'minecraft:stone' && dimOf(p) == 'minecraft:overworld', 'arena', 'player at arena in overworld')
})

// ---------------------------------------------------------------- kiln recipe execution (vanilla furnace slots), vanilla furnace as control
const KX = 2005, KZ = 1990, FX = 2007, FZ = 1990
function slotItem(data, slot) {
  let items = data.getList('Items', 10)
  for (let i = 0; i < items.size(); i++) { let c = items.getCompound(i); if (c.getByte('Slot') == slot) return String(c.getString('id')) + ' x' + c.getInt('count') }
  return 'none'
}
at(18, 'kiln clear', (s, p) => {
  cmd(s, 'execute in minecraft:overworld run setblock ' + KX + ' ' + AY + ' ' + KZ + ' minecraft:air')
  cmd(s, 'execute in minecraft:overworld run setblock ' + FX + ' ' + AY + ' ' + FZ + ' minecraft:air')
})
at(20, 'kiln setup', (s, p) => {
  // items go in through the container API (like a hopper/player would) so the furnace logic computes the cook time
  cmd(s, 'execute in minecraft:overworld run setblock ' + KX + ' ' + AY + ' ' + KZ + ' valoria:kiln')
  cmd(s, 'execute in minecraft:overworld run setblock ' + FX + ' ' + AY + ' ' + FZ + ' minecraft:furnace')
  cmd(s, 'execute in minecraft:overworld run item replace block ' + KX + ' ' + AY + ' ' + KZ + ' container.1 with minecraft:coal')
  cmd(s, 'execute in minecraft:overworld run item replace block ' + KX + ' ' + AY + ' ' + KZ + ' container.0 with valoria:void_stone')
  cmd(s, 'execute in minecraft:overworld run item replace block ' + FX + ' ' + AY + ' ' + FZ + ' container.1 with minecraft:coal')
  cmd(s, 'execute in minecraft:overworld run item replace block ' + FX + ' ' + AY + ' ' + FZ + ' container.0 with minecraft:cobblestone')
})
at(22, 'kiln items inserted', (s, p) => {
  let k = block(s, 'minecraft:overworld', KX, AY, KZ), f = block(s, 'minecraft:overworld', FX, AY, FZ)
  log(slotItem(k.entityData, 0).indexOf('valoria:void_stone') == 0, 'kiln accepts items through the container API', 'kiln=' + String(k.entityData) + ' furnace=' + String(f.entityData))
})
at(60, 'kiln lit', (s, p) => {
  let k = block(s, 'minecraft:overworld', KX, AY, KZ), f = block(s, 'minecraft:overworld', FX, AY, FZ)
  log(String(k.blockState).indexOf('lit=true') >= 0, 'kiln lights up with input+fuel', 'kiln ' + String(k.blockState) + ' ' + String(k.entityData) + ' | furnace ' + String(f.blockState))
})
at(250, 'kiln result', (s, p) => {
  let k = block(s, 'minecraft:overworld', KX, AY, KZ), f = block(s, 'minecraft:overworld', FX, AY, FZ)
  let out = slotItem(k.entityData, 2)
  log(out.indexOf('valoria:void_stone_brick') == 0, 'kiln smelts void_stone -> void_stone_brick (100 ticks)', 'kiln output: ' + out + ' state=' + String(k.blockState) + ' data=' + String(k.entityData) + ' | control furnace (200 ticks) output: ' + slotItem(f.entityData, 2))
})

// ---------------------------------------------------------------- dispenser + throwable
at(30, 'dispenser setup', (s, p) => {
  cmd(s, 'execute in minecraft:overworld run setblock 2010 ' + AY + ' 2010 minecraft:dispenser[facing=up]{Items:[{Slot:0b,id:"valoria:nature_arrow",count:1}]}')
  cmd(s, 'execute in minecraft:overworld run setblock 2011 ' + AY + ' 2010 minecraft:redstone_block')
})
at(34, 'dispenser early', (s, p) => { before.arrowsEarly = entitiesOfType(s, 'valoria:nature_arrow') })
at(40, 'dispenser result', (s, p) => {
  let n = Math.max(before.arrowsEarly || 0, entitiesOfType(s, 'valoria:nature_arrow'))
  log(n > 0, 'dispenser fires nature_arrow as a valoria:nature_arrow projectile (DispenserBehaviours)', n + ' arrow entities seen within 10 ticks')
  cmd(s, 'execute in minecraft:overworld run setblock 2011 ' + AY + ' 2010 minecraft:air')
  cmd(s, 'execute in minecraft:overworld run setblock 2010 ' + AY + ' 2010 minecraft:air')
})

// ---------------------------------------------------------------- music disc
at(50, 'disc', (s, p) => {
  cmd(s, 'item replace entity @a weapon.mainhand with valoria:music_disc_necromancer')
  cmd(s, 'execute in minecraft:overworld run setblock 2010 ' + AY + ' 1990 minecraft:air')
  cmd(s, 'execute in minecraft:overworld run setblock 2010 ' + AY + ' 1990 minecraft:jukebox')
  cmd(s, 'execute in minecraft:overworld run item replace block 2010 ' + AY + ' 1990 container.0 with valoria:music_disc_necromancer')
})
at(55, 'disc result', (s, p) => {
  let held = String(p.mainHandItem.toItemString ? p.mainHandItem.toItemString() : p.mainHandItem)
  let comps = String(p.mainHandItem.components)
  log(comps.indexOf('jukebox_playable') >= 0, 'music disc has jukebox_playable', comps)
  let jb = block(s, 'minecraft:overworld', 2010, AY, 1990)
  let rec = String(jb.entityData.getCompound('RecordItem').getString('id'))
  let ticks = jb.entityData.getLong('ticks_since_song_started')
  log(rec == 'valoria:music_disc_necromancer' && ticks > 0, 'jukebox plays the disc (song ticking)', String(jb.blockState) + ' record=' + rec + ' ticks_since_song_started=' + ticks)
  cmd(s, 'clear @a')
})

// ---------------------------------------------------------------- enchantments
at(60, 'enchants', (s, p) => {
  cmd(s, 'item replace entity @a weapon.mainhand with minecraft:iron_sword')
  cmd(s, 'enchant @a valoria:bleeding 1')
})
at(63, 'enchant bleeding result', (s, p) => {
  let c = String(p.mainHandItem.components)
  log(c.indexOf('valoria:bleeding') >= 0, '/enchant valoria:bleeding on iron_sword', c)
  cmd(s, 'item replace entity @a weapon.mainhand with valoria:blaze_reap')
  cmd(s, 'enchant @a valoria:explosive_flame 1')
})
at(66, 'enchant explosive_flame result', (s, p) => {
  let c = String(p.mainHandItem.components)
  log(c.indexOf('valoria:explosive_flame') >= 0, '/enchant valoria:explosive_flame on blaze_reap (#valoria:enchantable/blaze)', c)
  cmd(s, 'item replace entity @a weapon.mainhand with valoria:phantasm_bow')
  cmd(s, 'enchant @a valoria:accuracy 1')
})
at(69, 'enchant accuracy result', (s, p) => {
  let c = String(p.mainHandItem.components)
  log(c.indexOf('valoria:accuracy') >= 0, '/enchant valoria:accuracy on phantasm_bow (#valoria:enchantable/accuracy)', c)
  cmd(s, 'item replace entity @a weapon.mainhand with valoria:blaze_reap[minecraft:enchantments={levels:{"minecraft:fire_aspect":1}}]')
  cmd(s, 'enchant @a valoria:explosive_flame 1')
})
at(72, 'enchant exclusivity result', (s, p) => {
  let c = String(p.mainHandItem.components)
  log(c.indexOf('valoria:explosive_flame') < 0 && c.indexOf('fire_aspect') >= 0, 'explosive_flame refused on a fire_aspect blaze_reap (exclusive set)', c)
  cmd(s, 'item replace entity @a weapon.mainhand with minecraft:iron_sword')
  cmd(s, 'enchant @a valoria:explosive_flame 1')
})
at(75, 'enchant wrong item result', (s, p) => {
  let c = String(p.mainHandItem.components)
  log(c.indexOf('valoria:explosive_flame') < 0, 'explosive_flame refused on iron_sword (not in supported_items)', c)
  cmd(s, 'clear @a')
})

// ---------------------------------------------------------------- curio attribute bonus, three equip paths
// (1) stacks.setStackInSlot = what Curios' right-click-to-equip handler does (tick change detection applies modifiers)
// (2) ICuriosItemHandler.setEquippedCurio = API used by the Curios menu / other mods
// (3) /curios replace + /curios clear commands
function mh(p) { return Math.round(p.getAttributeTotalValue('minecraft:generic.max_health') * 1000) / 1000 }
function mods(p) { return String(p.getAttribute('minecraft:generic.max_health').getModifiers()) }
function curiosInv(p) { return Java.loadClass('top.theillusivec4.curios.api.CuriosApi').getCuriosInventory(p).get() }
function necklaceStacks(p) { return curiosInv(p).getStacksHandler('necklace').get().getStacks() }
function resetHealthMods(p) { p.getAttribute('minecraft:generic.max_health').removeModifiers() }
const CURIO_PATHS = [
  { name: 'right-click path (stacks.setStackInSlot)', equip: (s, p) => necklaceStacks(p).setStackInSlot(0, Item.of('valoria:iron_necklace_ruby')), unequip: (s, p) => necklaceStacks(p).setStackInSlot(0, Item.of('minecraft:air')) },
  { name: 'API path (setEquippedCurio)', equip: (s, p) => curiosInv(p).setEquippedCurio('necklace', 0, Item.of('valoria:iron_necklace_ruby')), unequip: (s, p) => curiosInv(p).setEquippedCurio('necklace', 0, Item.of('minecraft:air')) },
  // Curios' own debug command; it silently did nothing when issued from a script a few ticks after /curios clear, so it is informational
  { name: 'command path (/curios replace, /curios clear)', informational: true, equip: (s, p) => cmd(s, 'curios replace necklace 0 @a with valoria:iron_necklace_ruby'), unequip: (s, p) => cmd(s, 'curios clear @a') }
]
let ct = 76
CURIO_PATHS.forEach(path => {
  const t = ct
  at(t - 3, 'curio ' + path.name + ' clear', (s, p) => { cmd(s, 'curios clear @a') })
  at(t, 'curio ' + path.name + ' equip', (s, p) => {
    try {
      resetHealthMods(p)
      before.curioBase = mh(p)
      info('alive=' + p.isAlive() + ' hp=' + p.health + ' before equip via ' + path.name)
      path.equip(s, p)
    } catch (e) { log(false, 'curio ' + path.name + ' equip call', '' + e) }
  })
  at(t + 2, 'curio ' + path.name + ' prev', (s, p) => {
    try { info(path.name + ': slot=' + necklaceStacks(p).getStackInSlot(0) + ' previous=' + necklaceStacks(p).getPreviousStackInSlot(0) + ' modifiers=' + mods(p)) } catch (e) { info('prev read failed: ' + e) }
  })
  const report = path.informational ? (ok, name, detail) => info((ok ? 'ok ' : 'not applied ') + name + ' - ' + detail) : log
  at(t + 40, 'curio ' + path.name + ' equipped', (s, p) => {
    let now = mh(p)
    report(Math.abs(now - (before.curioBase + 1)) < 0.01, 'iron_necklace_ruby +1 max health via ' + path.name, before.curioBase + ' -> ' + now + ' modifiers=' + mods(p))
    try { path.unequip(s, p) } catch (e) { log(false, 'curio ' + path.name + ' unequip call', '' + e) }
  })
  at(t + 80, 'curio ' + path.name + ' unequipped', (s, p) => {
    let now = mh(p)
    report(Math.abs(now - before.curioBase) < 0.01, 'max health restored after unequip via ' + path.name, before.curioBase + ' -> ' + now + ' modifiers=' + mods(p))
  })
  ct += 84
})
// health necklace (ADD_MULTIPLIED_TOTAL) through the right-click path, to separate operation from path
at(ct, 'curio health equip', (s, p) => {
  try { cmd(s, 'curios clear @a'); resetHealthMods(p); before.healthBase = mh(p); necklaceStacks(p).setStackInSlot(0, Item.of('valoria:iron_necklace_health')) } catch (e) { log(false, 'health equip', '' + e) }
})
at(ct + 40, 'curio health equipped', (s, p) => {
  let now = mh(p)
  log(Math.abs(now - before.healthBase * 1.05) < 0.01, 'iron_necklace_health +5% max health via right-click path', before.healthBase + ' -> ' + now + ' modifiers=' + mods(p))
  try { necklaceStacks(p).setStackInSlot(0, Item.of('minecraft:air')) } catch (e) { info('health unequip: ' + e) }
})
at(ct + 80, 'curio health removed', (s, p) => {
  let now = mh(p)
  log(Math.abs(now - before.healthBase) < 0.01, 'max health restored after removing the health necklace', before.healthBase + ' -> ' + now + ' modifiers=' + mods(p))
})
ct += 84
// eye necklace: state-dependent modifiers (light +5% / dark -5%), through the right-click path
at(ct, 'curio eye equip', (s, p) => {
  try { cmd(s, 'curios clear @a'); resetHealthMods(p); before.eyeBase = mh(p); necklaceStacks(p).setStackInSlot(0, Item.of('valoria:iron_eye_necklace')) } catch (e) { log(false, 'eye equip', '' + e) }
})
at(ct + 60, 'curio eye equipped', (s, p) => {
  let now = mh(p)
  log(Math.abs(now - before.eyeBase) > 0.01, 'iron_eye_necklace changes max health (+5% light / -5% dark)', before.eyeBase + ' -> ' + now + ' modifiers=' + mods(p))
  try { necklaceStacks(p).setStackInSlot(0, Item.of('minecraft:air')) } catch (e) { info('eye unequip: ' + e) }
})
at(ct + 100, 'curio eye removed', (s, p) => {
  let now = mh(p)
  log(Math.abs(now - before.eyeBase) < 0.01, 'max health restored after removing the eye necklace (no modifier leak)', before.eyeBase + ' -> ' + now + ' modifiers=' + mods(p))
  resetHealthMods(p)
})

// ---------------------------------------------------------------- paintings
at(90, 'painting', (s, p) => {
  cmd(s, 'execute in minecraft:overworld run fill 2005 ' + (AY + 1) + ' 2014 2011 ' + (AY + 6) + ' 2014 minecraft:stone')
  cmd(s, 'execute in minecraft:overworld run summon minecraft:painting 2008 ' + (AY + 2) + ' 2013 {variant:"valoria:sauron",facing:2b}')
})
at(93, 'painting result', (s, p) => {
  log(entitiesOfType(s, 'minecraft:painting') > 0, 'valoria painting variant summons', 'variant valoria:sauron')
  cmd(s, 'kill @e[type=minecraft:painting]')
})

// ---------------------------------------------------------------- portal (frames face inward, 5x5 ring, 3x3 interior)
at(100, 'portal build', (s, p) => {
  const y = AY, cx = 2000, cz = 2005
  const f = (x, z, facing) => cmd(s, 'execute in minecraft:overworld run setblock ' + x + ' ' + y + ' ' + z + ' valoria:valoria_portal_frame[facing=' + facing + ']')
  for (let x = cx - 1; x <= cx + 1; x++) f(x, cz - 2, 'south')  // north row faces south (inward)
  for (let x = cx - 1; x <= cx + 1; x++) f(x, cz + 2, 'north')  // south row
  for (let z = cz - 1; z <= cz + 1; z++) f(cx - 2, z, 'east')   // west column
  for (let z = cz - 1; z <= cz; z++) f(cx + 2, z, 'west')        // east column, last block placed below
  f(cx + 2, cz + 1, 'west')
})
at(110, 'portal formed', (s, p) => {
  let id = String(block(s, 'minecraft:overworld', 2000, AY, 2005).id)
  log(id == 'valoria:valoria_portal', 'portal interior fills when ring completes', 'centre block: ' + id)
  if (id != 'valoria:valoria_portal') {
    // fallback: try outward-facing frames
    const y = AY, cx = 2000, cz = 2005
    const f = (x, z, facing) => cmd(s, 'execute in minecraft:overworld run setblock ' + x + ' ' + y + ' ' + z + ' valoria:valoria_portal_frame[facing=' + facing + ']')
    for (let x = cx - 1; x <= cx + 1; x++) f(x, cz - 2, 'north')
    for (let x = cx - 1; x <= cx + 1; x++) f(x, cz + 2, 'south')
    for (let z = cz - 1; z <= cz + 1; z++) f(cx - 2, z, 'west')
    for (let z = cz - 1; z <= cz + 1; z++) f(cx + 2, z, 'east')
  }
})
at(115, 'portal fallback check', (s, p) => {
  let id = String(block(s, 'minecraft:overworld', 2000, AY, 2005).id)
  info('portal centre after fallback: ' + id)
  if (id == 'valoria:valoria_portal') {
    before.portalOk = true
    cmd(s, 'execute in minecraft:overworld run tp @a 2000.5 ' + AY + ' 2005.5')
  }
})
at(160, 'portal travel out', (s, p) => {
  let d = dimOf(p)
  log(d == 'valoria:the_valoria', 'creative player entering portal arrives in valoria:the_valoria', 'dimension=' + d + ' pos=' + p.x.toFixed(1) + ',' + p.y.toFixed(1) + ',' + p.z.toFixed(1))
  if (d == 'valoria:the_valoria') {
    // look for the generated return portal around the player
    let lvl = s.getLevel('valoria:the_valoria')
    let found = null
    let px = Math.floor(p.x), py = Math.floor(p.y), pz = Math.floor(p.z)
    for (let dx = -8; dx <= 8 && !found; dx++) for (let dy = -6; dy <= 6 && !found; dy++) for (let dz = -8; dz <= 8 && !found; dz++) {
      if (String(lvl.getBlock(px + dx, py + dy, pz + dz).id) == 'valoria:valoria_portal') found = [px + dx, py + dy, pz + dz]
    }
    log(found != null, 'return portal generated in Valoria', found ? found.join(',') : 'none within 8 blocks')
    before.returnPortal = found
  }
})
at(200, 'portal travel back', (s, p) => {
  if (before.returnPortal) {
    let f = before.returnPortal
    cmd(s, 'execute in valoria:the_valoria run tp @a ' + (f[0] + 0.5) + ' ' + f[1] + ' ' + (f[2] + 0.5))
  }
})
at(260, 'portal back result', (s, p) => {
  let d = dimOf(p)
  log(d == 'minecraft:overworld', 'return trip lands in overworld', 'dimension=' + d + ' pos=' + p.x.toFixed(1) + ',' + p.y.toFixed(1) + ',' + p.z.toFixed(1) + ' (portal at 2000,' + AY + ',2005)')
  cmd(s, 'execute in minecraft:overworld run tp @a ' + AX + ' ' + AY + ' ' + AZ + ' 0 0')
  cmd(s, 'execute in minecraft:overworld run fill 1998 ' + AY + ' 2003 2002 ' + AY + ' 2007 minecraft:air')
})

// ---------------------------------------------------------------- nihility damage + max action (via the attachment API)
var ValoriaAttachments = null
function attClass() { if (ValoriaAttachments == null) ValoriaAttachments = Java.loadClass('com.idark.valoria.core.capability.ValoriaAttachments'); return ValoriaAttachments }
at(262, 'nihility set', (s, p) => {
  if (!ensureAlive(s, p)) { info('player was dead before the nihility test; respawned, skipping'); return }
  try {
    let lvl = p.getData(attClass().NIHILITY.get())
    let max = lvl.getMaxAmount(p)
    cmd(s, 'effect clear @a minecraft:resistance')
    cmd(s, 'effect clear @a minecraft:regeneration')
    cmd(s, 'attribute @a minecraft:generic.max_health base set 300')
    cmd(s, 'effect give @a minecraft:instant_health 1 30 true')
    cmd(s, 'gamemode survival @a')
    lvl.setAmountFromServer(p, max * 0.6) // >= 50% => periodic voidHarm damage, well below the 95% kill threshold
    before.nihilityHp = p.health
    info('nihility set to 60% of ' + max + ', player hp ' + before.nihilityHp)
  } catch (e) { log(false, 'nihility attachment reachable from script', '' + e) }
})
at(330, 'nihility damage result', (s, p) => {
  log(p.health < before.nihilityHp && p.health > 0, 'nihility above 50% damages a survival player (voidHarm) without killing at 60%', before.nihilityHp + ' -> ' + p.health)
  try { p.getData(attClass().NIHILITY.get()).setAmountFromServer(p, 0) } catch (e) { info('reset nihility failed: ' + e) }
  cmd(s, 'gamemode creative @a')
  cmd(s, 'attribute @a minecraft:generic.max_health base set 20')
  cmd(s, 'effect give @a minecraft:resistance 99999 4 true')
  cmd(s, 'effect give @a minecraft:regeneration 99999 4 true')
  cmd(s, 'effect give @a minecraft:instant_health 1 10 true')
})

// ---------------------------------------------------------------- all living entities spawn (glass box)
const MOBS = ['scourge', 'swamp_wanderer', 'goblin', 'devil', 'troll', 'corrupted_troll', 'haunted_merchant', 'draugr', 'shadewood_spider', 'mannequin', 'sorcerer', 'ent', 'nature_golem', 'river_golem', 'maggot', 'corrupted', 'king_crab', 'wicked_scorpion', 'scavenger', 'undead', 'flesh_sentinel', 'pixie']
at(280, 'mob spawn', (s, p) => {
  cmd(s, 'execute in minecraft:overworld run fill 1980 ' + AY + ' 1980 1992 ' + (AY + 6) + ' 1992 minecraft:glass hollow')
  MOBS.forEach(m => cmd(s, 'execute in minecraft:overworld run summon valoria:' + m + ' 1986 ' + (AY + 1) + ' 1986 {NoAI:1b,PersistenceRequired:1b}'))
})
at(320, 'mob spawn result', (s, p) => {
  let missing = []
  MOBS.forEach(m => { if (entitiesOfType(s, 'valoria:' + m) == 0) missing.push(m) })
  log(missing.length == 0, 'all ' + MOBS.length + ' living entity types exist 40 ticks after summon (shared box)', missing.length ? 'missing: ' + missing.join(',') : 'all present')
  before.missingMobs = missing
  cmd(s, 'kill @e[type=!minecraft:player,distance=..100]')
  cmd(s, 'execute in minecraft:overworld run fill 1980 ' + AY + ' 1980 1992 ' + (AY + 6) + ' 1992 minecraft:air')
})
// anything missing from the shared box is re-tested alone (other mobs' auras/attacks excluded)
at(325, 'mob solo spawn', (s, p) => {
  (before.missingMobs || []).forEach((m, i) => cmd(s, 'execute in minecraft:overworld run summon valoria:' + m + ' ' + (1980 + i * 4) + ' ' + (AY + 1) + ' 1980 {NoAI:1b,PersistenceRequired:1b}'))
})
at(345, 'mob solo result', (s, p) => {
  (before.missingMobs || []).forEach(m => {
    let n = entitiesOfType(s, 'valoria:' + m)
    let hp = 'n/a'
    s.entities.forEach(e => { if (String(e.type) == 'valoria:' + m) hp = e.health })
    log(n > 0, 'valoria:' + m + ' survives 20 ticks when summoned alone', 'present=' + n + ' hp=' + hp)
  })
  cmd(s, 'kill @e[type=!minecraft:player,distance=..100]')
})

// ---------------------------------------------------------------- boss fights: 15 s of AI against a survival player, then a player kill for loot
const BOSSES = [
  { id: 'necromancer', loot: 'valoria:necromancer_treasure_bag' },
  { id: 'dryador', loot: 'valoria:dryador_treasure_bag' },
  { id: 'firron', loot: 'valoria:firron_treasure_bag' },
  { id: 'wicked_crystal', loot: 'valoria:wicked_crystal_treasure_bag' }
]
let bt = 360
BOSSES.forEach(b => {
  const start = bt
  at(start, 'boss ' + b.id + ' summon', (s, p) => {
    ensureAlive(s, p)
    cmd(s, 'kill @e[type=minecraft:item]')
    cmd(s, 'gamemode survival @a')
    cmd(s, 'execute in minecraft:overworld run tp @a ' + AX + ' ' + AY + ' ' + AZ + ' 0 0')
    cmd(s, 'execute in minecraft:overworld run summon valoria:' + b.id + ' ' + AX + ' ' + AY + ' ' + (AZ - 6) + ' {PersistenceRequired:1b}')
  })
  at(start + 5, 'boss ' + b.id + ' alive', (s, p) => {
    log(entitiesOfType(s, 'valoria:' + b.id) == 1, 'boss ' + b.id + ' summoned', '')
  })
  at(start + 300, 'boss ' + b.id + ' after 15s', (s, p) => {
    let alive = entitiesOfType(s, 'valoria:' + b.id)
    log(alive == 1 && p.health > 0, 'boss ' + b.id + ' fought a survival player for 15 s without vanishing', 'player hp ' + p.health.toFixed(1) + ', boss present=' + alive)
    // generic_kill bypasses temporary invulnerability (e.g. Dryador during an animation) but still credits the player
    cmd(s, 'damage @e[type=valoria:' + b.id + ',limit=1] 100000 minecraft:generic_kill by @a[limit=1]')
  })
  at(start + 320, 'boss ' + b.id + ' loot', (s, p) => {
    let drops = itemDrops(s)
    // a drop that lands next to the survival player is picked up before we look, so count the inventory too
    let inInv = 0
    p.inventory.items.forEach(st => { if (String(st.id) == b.loot) inInv += st.count })
    if (entitiesOfType(s, 'valoria:' + b.id) > 0) info('boss ' + b.id + ' still present 20 ticks after the kill damage')
    log(drops.indexOf(b.loot) >= 0 || inInv > 0, 'boss ' + b.id + ' drops ' + b.loot + ' when killed by the player', 'drops on ground: ' + (drops.length ? drops.join(',') : 'none') + '; in player inventory: ' + inInv)
    cmd(s, 'clear @a')
    info('codex pages after killing ' + b.id + ': ' + attachments(p))
    cmd(s, 'kill @e[type=!minecraft:player,distance=..100]')
  })
  bt = start + 340
})

// ---------------------------------------------------------------- structure placement + interior scan + dungeon-visit codex unlock
const SX = 2400, SZ = 2400
at(bt, 'structure place', (s, p) => {
  cmd(s, 'gamemode creative @a')
  cmd(s, 'execute in minecraft:overworld run forceload add ' + (SX - 96) + ' ' + (SZ - 96) + ' ' + (SX + 96) + ' ' + (SZ + 96))
  cmd(s, 'execute in minecraft:overworld run place structure valoria:crypt ' + SX + ' 64 ' + SZ)
})
at(bt + 40, 'structure scan', (s, p) => {
  let lvl = s.getLevel('minecraft:overworld')
  let valoria = 0, chests = 0, pots = 0, lootChests = 0, sample = {}
  let firstChest = null
  for (let x = SX - 90; x <= SX + 90; x += 2) for (let z = SZ - 90; z <= SZ + 90; z += 2) for (let y = -40; y <= 110; y += 1) {
    let id = String(lvl.getBlock(x, y, z).id)
    if (id.indexOf('valoria:') == 0) { valoria++; sample[id] = (sample[id] || 0) + 1; if (id.indexOf('pot') >= 0) pots++ }
    if (id == 'minecraft:chest' || id == 'minecraft:barrel' || id.indexOf('sarcophagus') >= 0) {
      chests++
      let d = lvl.getBlock(x, y, z).entityData
      if (d != null && String(d).indexOf('LootTable') >= 0) { lootChests++; if (!firstChest) firstChest = [x, y, z] }
    }
  }
  let top = Object.keys(sample).sort((a, b) => sample[b] - sample[a]).slice(0, 6).map(k => k + '=' + sample[k]).join(' ')
  log(valoria > 100, '/place structure valoria:crypt generates Valoria blocks', valoria + ' valoria blocks sampled; ' + top)
  log(chests > 0, 'crypt contains containers', chests + ' containers, ' + lootChests + ' with LootTable' + (firstChest ? ' e.g. ' + firstChest.join(',') : ''))
  before.structChest = firstChest
})
at(bt + 45, 'structure visit', (s, p) => {
  let c = before.structChest || [SX, 60, SZ]
  cmd(s, 'execute in minecraft:overworld run tp @a ' + c[0] + ' ' + (c[1] + 1) + ' ' + c[2])
  before.attachmentsBeforeVisit = attachments(p)
  try {
    // which structures does the structure manager know about at the chest? (tells whether /place registered the start)
    let lvl = s.getLevel('minecraft:overworld')
    let BlockPos = Java.loadClass('net.minecraft.core.BlockPos')
    let all = lvl.structureManager().getAllStructuresAt(new BlockPos(c[0], c[1] + 1, c[2]))
    let names = []
    all.keySet().forEach(st => names.push(String(lvl.registryAccess().registryOrThrow(Java.loadClass('net.minecraft.core.registries.Registries').STRUCTURE).getKey(st))))
    // /place structure writes blocks only (no StructureStart in the chunk references), so this is expected to be empty
    info('structure references at the placed crypt chest (expected none for /place): ' + (names.length ? names.join(',') : 'none'))
  } catch (e) { info('structure lookup failed: ' + e) }
})
// call Valoria's own dungeon-visit check directly at several heights around the chest (the tick handler only runs every 100 ticks)
function cryptUnlockable() { return Java.loadClass('com.idark.valoria.client.ui.screen.book.RegisterUnlockables').crypt }
at(bt + 50, 'structure visit check', (s, p) => {
  try {
    let lvl = s.getLevel('minecraft:overworld')
    cryptUnlockable().checkCondition(p, lvl)
    info('direct checkCondition at ' + p.x.toFixed(0) + ',' + p.y.toFixed(0) + ',' + p.z.toFixed(0) + ' -> pages: ' + attachments(p))
  } catch (e) { log(false, 'crypt unlockable reachable from script', '' + e) }
})
at(bt + 200, 'structure visit result', (s, p) => {
  // /place structure does not register a StructureStart in the chunk references, so the codex handler cannot see it;
  // the real check is done against a naturally generated crypt below. Logged for information only.
  info('pages after standing in the /place-d crypt (expected unchanged): ' + attachments(p))
})
// natural crypt: candidates = /locate result from the first manual session plus the nearest placement from the arena.
// For each candidate chunk, read the structure start (references + getStructureAt), stand in its first piece and run the unlock check.
const CRYPT_CANDIDATES = [[-560, -752], [-1040, -1136], [-1024, 288]]
at(bt + 205, 'natural crypt locate', (s, p) => {
  try {
    let lvl = s.getLevel('minecraft:overworld')
    let RL = Java.loadClass('net.minecraft.resources.ResourceLocation')
    let Registries = Java.loadClass('net.minecraft.core.registries.Registries')
    let TagKey = Java.loadClass('net.minecraft.tags.TagKey')
    let BlockPos = Java.loadClass('net.minecraft.core.BlockPos')
    let tag = TagKey.create(Registries.STRUCTURE, RL.parse('valoria:crypts'))
    let pos = lvl.findNearestMapStructure(tag, new BlockPos(AX, AY, AZ), 100, false)
    if (pos != null) CRYPT_CANDIDATES.push([pos.getX(), pos.getZ()])
    info('crypt candidates: ' + JSON.stringify(CRYPT_CANDIDATES))
    before.cryptStart = null
    let reg = lvl.registryAccess().registryOrThrow(Registries.STRUCTURE)
    CRYPT_CANDIDATES.forEach(c => {
      if (before.cryptStart) return
      cmd(s, 'execute in minecraft:overworld run forceload add ' + (c[0] - 32) + ' ' + (c[1] - 32) + ' ' + (c[0] + 32) + ' ' + (c[1] + 32))
      for (let y = 100; y >= -40 && !before.cryptStart; y -= 8) {
        let bp = new BlockPos(c[0] + 8, y, c[1] + 8)
        let refs = lvl.structureManager().getAllStructuresAt(bp)
        refs.keySet().forEach(st => {
          if (before.cryptStart) return
          let key = String(reg.getKey(st))
          if (key.indexOf('crypt') >= 0) {
            let start = lvl.structureManager().getStructureAt(bp, st)
            if (start.isValid()) { before.cryptStart = start; before.cryptKey = key }
          }
        })
      }
    })
    log(before.cryptStart != null, 'a naturally generated crypt start was resolved', before.cryptStart ? before.cryptKey + ' bounds=' + before.cryptStart.getBoundingBox() + ' pieces=' + before.cryptStart.getPieces().size() : 'none at any candidate')
    if (before.cryptStart) {
      let c = before.cryptStart.getPieces().get(0).getBoundingBox().getCenter()
      cmd(s, 'execute in minecraft:overworld run tp @a ' + c.getX() + ' ' + c.getY() + ' ' + c.getZ())
      info('standing in piece 0 centre ' + c)
    }
  } catch (e) { log(false, 'natural crypt lookup from script', '' + e) }
})
at(bt + 215, 'natural crypt direct check', (s, p) => {
  if (!before.cryptStart) return
  try { cryptUnlockable().checkCondition(p, s.getLevel('minecraft:overworld')); info('direct checkCondition at ' + p.x.toFixed(0) + ',' + p.y.toFixed(0) + ',' + p.z.toFixed(0) + ' -> ' + attachments(p)) } catch (e) { info('check failed: ' + e) }
})
at(bt + 335, 'natural crypt visit result', (s, p) => {
  let after = attachments(p)
  log(after.indexOf('valoria:crypt') >= 0, 'standing inside a naturally generated crypt unlocks the codex page (direct check + 120 ticks of the server handler)', 'pages: ' + after)
})

// ---------------------------------------------------------------- done
at(bt + 345, 'done', (s, p) => {
  cmd(s, 'execute in minecraft:overworld run tp @a ' + AX + ' ' + AY + ' ' + AZ)
  cmd(s, 'gamemode creative @a')
  console.log(TAG + ' DONE ' + pass + '/' + total)
})

ServerEvents.tick(event => {
  const server = event.server
  if (server.players.isEmpty()) return
  if (!started) { started = true; t0 = server.tickCount; console.log(TAG + ' START tick ' + t0) }
  const t = server.tickCount - t0
  const player = server.players.get(0)
  steps.forEach(st => {
    if (st.tick == t) {
      try { st.fn(server, player) } catch (e) { log(false, st.name, 'exception: ' + e) }
    }
  })
})
