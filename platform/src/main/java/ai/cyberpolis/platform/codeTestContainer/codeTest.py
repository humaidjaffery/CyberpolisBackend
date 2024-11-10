# def runCode(arguments):
#     test_results = []

#     # Install packages, 
#     # Dont need this right now, docker image already installs all the basic packages --> numpy, matplotlib, pandas, etc. 
#     # maybe I can implement this later for more complex code assinments, but i dont think it will be needed cause i can always just install any required libraries in the docker image before build
# #     if 'packages' in arguments and arguments['packages']:
# #         for package in arguments['packages']:
# #             i = subprocess.run(['pip', 'install', package], capture_output=True, text=True)
# #             if i.returncode != 0:
# #                 print(f"UNABLE TO INSTALL PACKAGE {package}: {i.stderr}")

#     #user code
#     with open('userCode.py', 'w', encoding='utf-8') as f:
#         for line in arguments['code']:
#             f.write(line + '\n')

#     # # Execute tests
#     # for test in arguments['tests']:
#     #     p = subprocess.run(["python", "userCode.py"], capture_output=True, text=True, timeout=5)
#     #     exit_code = p.returncode
#     #     stdout = p.stdout.strip()
#     #     stderr = p.stderr.strip()

#     #     test_results.append({
#     #         "input": test[0],
#     #         "expected_output": test[-1],
#     #         "exit_code": exit_code,
#     #         "stdout": stdout,
#     #         "stderr": stderr,
#     #         "passed": passed
#     #     })

#     p = subprocess.run(["python", "userCode.py"], capture_output=True, text=True, timeout=5)

#     if p.returncode != 0:
#         print("ERROR RUNNING USER CODE: ")

#     # Output the test results as JSON
#     return {
#         "result": test_results
#     }



# if __name__ == '__main__':
    # print("TEST")
    # if len(sys.argv) < 1:
    #     print("ERROR, PLEASE PROVIDE ARGUMENT")
    #     sys.exit(10)

    # # Read input JSON from stdin
    # arguments = json.loads(sys.stdin.read())
    # print("ARGUMENTS:")
    # print(arguments)
    
    # #writing code to file
    # with open('userCode.py', 'w', encoding='utf-8') as f:
    #     for line in arguments['code']:
    #         f.write(line + '\n')

    # with open('userCode.py', 'r', encoding='utf-8') as f:
    #     print(f.read())

    # p = subprocess.run(["python", "userCode.py"], capture_output=True, text=True, timeout=5)

    # if p.returncode != 0:
    #     print("ERROR RUNNING USER CODE: " + p.returncode)
    #     print(p.stderr)
    #     print(p.stdout)

    # print(json.dumps(p.stdout))


import sys
import json
import subprocess    

if __name__ == '__main__':
    # Read input JSON from stdin if needed
    try:
        arguments = json.loads(sys.stdin.read())
        
        # Write code to file if needed
        with open('userCode.py', 'w', encoding='utf-8') as f:
            for line in arguments.get('code', []):
                f.write(line + '\n')
        
        # Run the code
        try:
            # Capture output and run the script
            result = subprocess.run([sys.executable, "userCode.py"], 
                                    capture_output=True, 
                                    text=True, 
                                    timeout=10)
                    
            # Try to parse the output as JSON
            try:
                #SUCCESS
                json_output = json.loads(result.stdout)
                print(json.dumps(json_output))
            except json.JSONDecodeError:
                #error response
                print(json.dumps({
                    "results": [
                        {
                            "name": "JSON Parsing Error",
                            "input": {},
                            "expected_output": None,
                            "stdout": result.stdout,
                            "stderr": "Failed to parse JSON output",
                            "passed": False,
                            "exit_code": result.returncode
                        }
                    ]
                }))
        
        except subprocess.TimeoutExpired:
            print(json.dumps({
                "results": [
                    {
                        "name": "Timeout Error",
                        "input": {},
                        "expected_output": None,
                        "stdout": "",
                        "stderr": "Code execution timed out",
                        "passed": False,
                        "exit_code": result.returncode
                    }
                ]
            }))
        except Exception as e:
            print(json.dumps({
                "results": [
                    {
                        "name": "Execution Error",
                        "input": {},
                        "expected_output": None,
                        "stdout": "",
                        "stderr": str(e),
                        "passed": False,
                        "exit_code": result.returncode
                    }
                ]
            }))
    
    except Exception as e:
        print(json.dumps({
            "results": [
                {
                    "name": "Preparation Error",
                    "input": {},
                    "expected_output": None,
                    "stdout": "",
                    "stderr": f"Error in preparing code: {str(e)}",
                    "passed": False,
                    "exit_code": result.returncode
                }
            ]
        }))
