package riscv
import chisel3.iotesters.PeekPokeTester

class if_id_reg_test(c:if_id_reg) extends PeekPokeTester(c){
	poke(c.io.pc_input,1)
	poke(c.io.pc4_input,2)
	poke (c.io.instruction_input,3)
	step(1)

}

